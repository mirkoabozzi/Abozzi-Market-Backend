package mirkoabozzi.Abozzi.Market.controllers;

import mirkoabozzi.Abozzi.Market.dto.UsersDTO;
import mirkoabozzi.Abozzi.Market.dto.UsersLoginDTO;
import mirkoabozzi.Abozzi.Market.dto.UsersLoginRespDTO;
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
    public User saveUser(@RequestBody @Validated UsersDTO payload, BindingResult validation) {
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
}
