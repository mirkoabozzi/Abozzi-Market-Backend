package mirkoabozzi.Abozzi.Market.seciurity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.enums.Role;
import mirkoabozzi.Abozzi.Market.repositories.UsersRepository;
import mirkoabozzi.Abozzi.Market.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class Oauth2LoginHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UsersService usersService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UsersRepository usersRepository;
    @Value("${cors.config.local.host}")
    private String localHost;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();

            Map<String, Object> attributes = principal.getAttributes();
            String name = attributes.getOrDefault("given_name", "").toString();
            String surname = attributes.getOrDefault("family_name", "").toString();
            String email = attributes.getOrDefault("email", "").toString();
            String picture = attributes.getOrDefault("picture", "").toString();
            String isVerified = attributes.getOrDefault("email_verified", "").toString();

            String idAttributeKey = "sub";

            User existingUser = this.usersRepository.findByEmail(email).orElse(null);
            if (existingUser != null) {
                DefaultOAuth2User oAuth2User = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(existingUser.getName())), attributes, idAttributeKey);
                Authentication securityAuth = new OAuth2AuthenticationToken(oAuth2User, List.of(new SimpleGrantedAuthority(existingUser.getName())), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
            } else {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setAvatar(picture);
                newUser.setSurname(surname);
                newUser.setIsVerified(Boolean.valueOf(isVerified));
                newUser.setRegistrationDate(LocalDateTime.now());
                newUser.setRole(Role.USER);
                this.usersRepository.save(newUser);

                DefaultOAuth2User oAuth2User = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(newUser.getName())), attributes, idAttributeKey);
                Authentication securityAuth = new OAuth2AuthenticationToken(oAuth2User, List.of(new SimpleGrantedAuthority(newUser.getName())), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
            }
        }
        this.setAlwaysUseDefaultTargetUrl(true);

        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        User userAuthenticated = this.usersService.findByEmail(email);

        String token = this.jwtTools.generateToken(userAuthenticated);

        String targetUrl = UriComponentsBuilder.fromUriString(localHost + "/").queryParam("token", token).build().toUriString();

        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);

    }
}
