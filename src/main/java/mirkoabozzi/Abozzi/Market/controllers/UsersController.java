package mirkoabozzi.Abozzi.Market.controllers;

import mirkoabozzi.Abozzi.Market.dto.ChangeUserPasswordDTO;
import mirkoabozzi.Abozzi.Market.dto.ResetPasswordRespDTO;
import mirkoabozzi.Abozzi.Market.dto.UsersDTO;
import mirkoabozzi.Abozzi.Market.dto.UsersRoleDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    //GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  @RequestParam(defaultValue = "surname") String sortBy) {
        return this.usersService.findAll(page, size, sortBy);
    }

    //GET ME
    @GetMapping("/me")
    public User getMyProfile(@AuthenticationPrincipal User userAuthenticated) {
        return userAuthenticated;
    }

    //PUT ME
    @PutMapping("/me")
    public User updateMyProfile(@AuthenticationPrincipal User userAuthenticated, @RequestBody UsersDTO payload) {
        return this.usersService.updateUser(userAuthenticated.getId(), payload);
    }

    //POST ME IMG
    @PostMapping("/me/avatar")
    public void imgUpload(@RequestParam("avatar") MultipartFile img, @AuthenticationPrincipal User userAuthenticated) throws IOException, MaxUploadSizeExceededException {
        this.usersService.imgUpload(img, userAuthenticated.getId());
    }

    //PUT CHANGE USER ROLE
    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUserRole(@RequestBody UsersRoleDTO payload) {
        return this.usersService.updateUserRole(payload);
    }

    //DELET USER
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable UUID id) {
        this.usersService.deleteUser(id);
    }


    //GET BY NAME
    @GetMapping("/name")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> findByName(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size,
                                 @RequestParam(defaultValue = "surname") String sortBy,
                                 @RequestParam String user) {
        return this.usersService.findByName(page, size, sortBy, user);
    }

    //PUT ME PASSWORD
    @PutMapping("/me/password")
    public ResetPasswordRespDTO updateMyPassword(@AuthenticationPrincipal User userAuthenticated, @RequestBody ChangeUserPasswordDTO payload) {
        this.usersService.changeUserPassword(userAuthenticated.getId(), payload);
        return new ResetPasswordRespDTO("Password changed");
    }
}
