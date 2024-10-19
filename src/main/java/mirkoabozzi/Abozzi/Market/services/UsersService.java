package mirkoabozzi.Abozzi.Market.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mirkoabozzi.Abozzi.Market.dto.*;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.enums.Role;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.UsersRepository;
import mirkoabozzi.Abozzi.Market.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${cors.config.local.host.router}")
    private String localHostRouter;

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

        MimeMessage msg = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(userFound.getEmail());
        helper.setSubject("Richiesta reset password");
        String content = "<h1>Password reset</h1>" +
                "<p>Hai richiesto il reset della password, al seguente link puoi cambiare la tua password!</p>" +
                "<p>" + localHostRouter + "/passwordReset/userId=" + userFound.getId() + "</p>" +
                "<p>Se non sei stato tu a richiedere il cambio password, ignora questa email!</p" +
                "<p>Abozzi Market</p>";
        helper.setText(content, true);
        this.javaMailSender.send(msg);
    }

    //RESET USER PASSWORD
    public void resetUserPassword(UUID id, ResetUserPassword payload) {
        User userFound = this.findById(id);
        userFound.setPassword(this.passwordEncoder.encode(payload.password()));
        this.usersRepository.save(userFound);
    }

    //CHANGE MY PASSWORD
    public void changeUserPassword(UUID id, ChangeUserPasswordDTO payload) {
        User userFound = this.findById(id);
        if (!this.passwordEncoder.matches(payload.oldPassword(), userFound.getPassword()))
            throw new BadRequestException("Wrong password");
        userFound.setPassword(this.passwordEncoder.encode(payload.newPassword()));
        this.usersRepository.save(userFound);
    }
}
