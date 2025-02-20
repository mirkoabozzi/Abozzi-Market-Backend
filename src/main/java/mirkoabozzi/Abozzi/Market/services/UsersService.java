package mirkoabozzi.Abozzi.Market.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.mail.MessagingException;
import mirkoabozzi.Abozzi.Market.dto.request.*;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.enums.RegistrationMethod;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailService mailService;

    //FIND BY EMAIL
    public User findByEmail(String email) {
        return this.usersRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email " + email + " not found on DB"));
    }

    //FIND BY ID
    public User findById(UUID id) {
        return this.usersRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found on DB"));
    }

    //POST SAVE
    public User saveUser(UsersDTO payload) throws MessagingException {
        if (this.usersRepository.existsByEmail(payload.email()))
            throw new BadRequestException("Email " + payload.email() + " already on DB");
        User newUser = new User(
                payload.name(),
                payload.surname(),
                payload.email(),
                this.passwordEncoder.encode(payload.password()),
                payload.phoneNumber(),
                "https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.surname(),
                RegistrationMethod.FORM);
        String token = UUID.randomUUID().toString();
        newUser.setVerificationToken(token);
        User userSaved = this.usersRepository.save(newUser);
//        this.mailgunSender.sendRegistrationEmail(newUser);
        this.mailService.userRegistrationEmail(userSaved);
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
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("secure_url");
        userFound.setAvatar(url);
        this.usersRepository.save(userFound);
    }

    //PUT UPDATE USER ROLE
    public User updateUserRole(UsersRoleDTO payload) {
        User userFound = this.findById(UUID.fromString(payload.user()));
        userFound.setRole(Role.valueOf(payload.role().toUpperCase()));
        return this.usersRepository.save(userFound);
    }

    //DELETE
    public void deleteUser(UUID id) {
        User userFound = this.findById(id);
        this.usersRepository.delete(userFound);
    }

    //FIND BY NAME
    public Page<User> findByName(int pages, int size, String sortBy, String user) {
        Pageable pageable = PageRequest.of(pages, size, Sort.by(sortBy));
        return this.usersRepository.findByNameContainingIgnoringCaseOrSurnameContainingIgnoreCase(pageable, user, user);
    }

    //RESET USER PASSWORD REQUEST
    public void resetUserPasswordRequest(ResetUserPasswordRequest payload) throws MessagingException {
        User userFound = this.findByEmail(payload.email());
        if (userFound.getRegistrationMethod() == RegistrationMethod.FORM)
            this.mailService.resetPasswordRequest(userFound);
        else throw new BadRequestException("Account registered with Google");
    }

    //RESET USER PASSWORD
    public void resetUserPassword(String token, ResetUserPassword payload) throws MessagingException {
        User userFound = this.usersRepository.findByResetPasswordToken(token).orElseThrow(() -> new BadRequestException("Invalid password reset token!"));
        if (!userFound.getTokenDuration().isAfter(LocalDateTime.now()))
            throw new BadRequestException("Password reset token expired!");
        userFound.setPassword(this.passwordEncoder.encode(payload.password()));
        userFound.setResetPasswordToken(null);
        userFound.setTokenDuration(null);
        this.usersRepository.save(userFound);
        this.mailService.confirmPasswordChanged(userFound);
    }

    //CHANGE MY PASSWORD
    public void changeUserPassword(UUID id, ChangeUserPasswordDTO payload) {
        User userFound = this.findById(id);
        if (!this.passwordEncoder.matches(payload.oldPassword(), userFound.getPassword()))
            throw new BadRequestException("Wrong password");
        userFound.setPassword(this.passwordEncoder.encode(payload.newPassword()));
        this.usersRepository.save(userFound);
    }

    //VERIFY USER EMAIL
    public void verifyUserEmail(String verificationToken) {
        User userFound = this.usersRepository.findByVerificationToken(String.valueOf(verificationToken)).orElseThrow(() -> new BadRequestException("User not found!"));
        userFound.setIsVerified(true);
        userFound.setVerificationToken(null);
        this.usersRepository.save(userFound);
    }
}
