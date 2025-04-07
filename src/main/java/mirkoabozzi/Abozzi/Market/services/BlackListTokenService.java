package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.entities.BlackListToken;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.repositories.BlackListTokenRepository;
import mirkoabozzi.Abozzi.Market.seciurity.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BlackListTokenService {
    @Autowired
    private BlackListTokenRepository blackListTokenRepository;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UsersService usersService;

    public Boolean isPresent(String token) {
        return this.blackListTokenRepository.findByToken(token).isPresent();
    }

    public void logout(String token) {
        if (this.isPresent(token))
            throw new BadRequestException("Token already invalidated");
        String userId = this.jwtTools.extractIdFromToken(token);
        User userFound = this.usersService.findById(UUID.fromString(userId));
        BlackListToken blackListToken = new BlackListToken(
                token,
                LocalDateTime.now(),
                userFound
        );
        this.blackListTokenRepository.save(blackListToken);
    }
}
