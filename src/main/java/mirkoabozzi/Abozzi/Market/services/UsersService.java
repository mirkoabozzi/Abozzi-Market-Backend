package mirkoabozzi.Abozzi.Market.services;

import com.cloudinary.Cloudinary;
import mirkoabozzi.Abozzi.Market.dto.UsersDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    //FIND BY EMAIL
    public User findByEmail(String email) {
        return this.usersRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email " + email + " not found on DB"));
    }

    //FIND BY ID
    public User findById(UUID id) {
        return this.usersRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found on DB"));
    }

    //POST SAVE
    public User saveUser(UsersDTO payload) {
        if (this.usersRepository.existsByEmail(payload.email()))
            throw new BadRequestException("Email " + payload.email() + " already on DB");
        User newUser = new User(
                payload.name(),
                payload.surname(),
                payload.email(),
                this.passwordEncoder.encode(payload.password()),
                payload.phoneNumber(), "https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.surname());
        User userSaved = this.usersRepository.save(newUser);
        return userSaved;
    }
}
