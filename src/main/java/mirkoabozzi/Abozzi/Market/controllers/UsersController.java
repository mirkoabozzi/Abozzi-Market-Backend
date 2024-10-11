package mirkoabozzi.Abozzi.Market.controllers;

import mirkoabozzi.Abozzi.Market.dto.UsersDTO;
import mirkoabozzi.Abozzi.Market.dto.UsersRoleDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    //GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "name") String sortBy) {
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

    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUserRole(@RequestBody UsersRoleDTO payload) {
        return this.usersService.updateUserRole(payload);
    }
}
