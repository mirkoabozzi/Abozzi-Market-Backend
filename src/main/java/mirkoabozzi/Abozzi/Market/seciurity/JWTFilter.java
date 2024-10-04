package mirkoabozzi.Abozzi.Market.seciurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.UnauthorizedException;
import mirkoabozzi.Abozzi.Market.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer "))
            throw new UnauthorizedException("Token required");
        String token = header.substring(7);
        this.jwtTools.verifyToken(token);
        String id = jwtTools.extractIdFromToken(token);
        User userFound = this.usersService.findById(UUID.fromString(id));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userFound, null, userFound.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        List<String> patternList = Arrays.asList("/authentication/**", "/categories");
        AntPathMatcher newAntPath = new AntPathMatcher();
        return patternList.stream().anyMatch(pattern -> newAntPath.match(pattern, path));
    }
}
