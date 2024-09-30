package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.UsersLoginDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.UnauthorizedException;
import mirkoabozzi.Abozzi.Market.seciurity.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTTools jwtTools;

    public String checkCredentialAndGenerateToken(UsersLoginDTO payload) {
        User userFound = this.usersService.findByEmail(payload.email());
        if (passwordEncoder.matches(payload.password(), userFound.getPassword())) {
            return this.jwtTools.generateToken(userFound);
        } else {
            throw new UnauthorizedException("Incorrect credentials");
        }
    }
}
