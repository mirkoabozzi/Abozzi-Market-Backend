package mirkoabozzi.Abozzi.Market.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import mirkoabozzi.Abozzi.Market.dto.UsersDTO;
import mirkoabozzi.Abozzi.Market.dto.UsersRoleDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.enums.Role;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.UsersRepository;
import mirkoabozzi.Abozzi.Market.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private MailgunSender mailgunSender;

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
//        this.mailgunSender.sendRegistrationEmail(newUser);
        return userSaved;
    }

    //GET ALL
    public Page<User> findAll(int page, int size, String sortBy) {
        if (page > 50) page = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.usersRepository.findAll(pageable);
    }

    //PUT UPDATE PROFILE
    public User updateUser(UUID id, UsersDTO payload) {
        User userFound = this.findById(id);
        if (!userFound.getEmail().equals(payload.email()) && this.usersRepository.existsByEmail(payload.email()))
            throw new BadRequestException("Email " + payload.email() + " already on DB");
        userFound.setName(payload.name());
        userFound.setSurname(payload.surname());
        userFound.setEmail(payload.email());
        userFound.setPhoneNumber(payload.phoneNumber());
        return this.usersRepository.save(userFound);
    }

    //IMG UPLOAD
    public void imgUpload(MultipartFile file, UUID id) throws IOException, MaxUploadSizeExceededException {
        User userFound = this.findById(id);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        userFound.setAvatar(url);
        this.usersRepository.save(userFound);
    }

    //PUT UPDATE USER ROLE
    public User updateUserRole(UsersRoleDTO payload) {
        User userFound = this.findById(UUID.fromString(payload.user()));
        userFound.setRole(Role.valueOf(payload.role().toUpperCase()));
        return this.usersRepository.save(userFound);
    }
}
