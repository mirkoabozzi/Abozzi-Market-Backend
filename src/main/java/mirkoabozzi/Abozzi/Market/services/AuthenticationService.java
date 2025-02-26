package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.request.UsersLoginDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.enums.RegistrationMethod;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
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

        if (userFound.getRegistrationMethod().equals(RegistrationMethod.GOOGLE))
            throw new BadRequestException("Account registered with Google");
        if (!passwordEncoder.matches(payload.password(), userFound.getPassword()))
            throw new UnauthorizedException("Incorrect credentials");
        if (!userFound.getIsVerified())
            throw new UnauthorizedException("Account not verified");
        else return this.jwtTools.generateToken(userFound);
    }
}
