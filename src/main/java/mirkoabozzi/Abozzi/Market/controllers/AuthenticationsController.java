package mirkoabozzi.Abozzi.Market.controllers;

import jakarta.mail.MessagingException;
import mirkoabozzi.Abozzi.Market.dto.*;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.AuthenticationService;
import mirkoabozzi.Abozzi.Market.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/authentication")
public class AuthenticationsController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UsersService usersService;

    //POST REGISTRATION
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody @Validated UsersDTO payload, BindingResult validation) throws MessagingException {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.usersService.saveUser(payload);
        }
    }

    //POST LOGIN
    @PostMapping("/login")
    public UsersLoginRespDTO login(@RequestBody @Validated UsersLoginDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return new UsersLoginRespDTO(this.authenticationService.checkCredentialAndGenerateToken(payload));
        }
    }

    //POST RESET PASSWORD REQUEST
    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResetPasswordRespDTO resetPasswordRequest(@RequestBody @Validated ResetUserPasswordRequest payload, BindingResult validation) throws MessagingException {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            this.usersService.resetUserPasswordRequest(payload);
        }
        return new ResetPasswordRespDTO("Reset password request accepted");
    }

    //PUT RESET PASSWORD
    @PutMapping("/reset/{token}")
    public ResetPasswordRespDTO resetPassword(@PathVariable String token, @RequestBody @Validated ResetUserPassword payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            this.usersService.resetUserPassword(token, payload);
        }
        return new ResetPasswordRespDTO("Password has been reset");
    }

    //PUT VERIFY USER
    @PutMapping("/verify/{token}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void verifyUserEmail(@PathVariable String token) {
        this.usersService.verifyUserEmail(token);
    }
}
