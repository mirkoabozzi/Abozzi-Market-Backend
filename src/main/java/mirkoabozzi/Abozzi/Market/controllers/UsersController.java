package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import mirkoabozzi.Abozzi.Market.dto.request.ChangeUserPasswordDTO;
import mirkoabozzi.Abozzi.Market.dto.request.UsersDTO;
import mirkoabozzi.Abozzi.Market.dto.request.UsersRoleDTO;
import mirkoabozzi.Abozzi.Market.dto.response.ResetPasswordRespDTO;
import mirkoabozzi.Abozzi.Market.dto.response.UserRespDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.services.UsersService;
import org.modelmapper.ModelMapper;
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
@Tag(name = "Users")
public class UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private ModelMapper modelMapper;

    //GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<UserRespDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size,
                                         @RequestParam(defaultValue = "surname") String sortBy
    ) {
        Page<User> userPage = this.usersService.findAll(page, size, sortBy);
        return userPage.map(user -> this.modelMapper.map(user, UserRespDTO.class));
    }

    //GET ME
    @GetMapping("/me")
    public UserRespDTO getMyProfile(@AuthenticationPrincipal User userAuthenticated) {
        return this.modelMapper.map(userAuthenticated, UserRespDTO.class);
    }

    //PUT ME
    @PutMapping("/me")
    public UserRespDTO updateMyProfile(@AuthenticationPrincipal User userAuthenticated, @RequestBody UsersDTO payload) {
        User user = this.usersService.updateUser(userAuthenticated.getId(), payload);
        return this.modelMapper.map(user, UserRespDTO.class);
    }

    //POST ME IMG
    @PostMapping("/me/avatar")
    public void imgUpload(@RequestParam("avatar") MultipartFile img, @AuthenticationPrincipal User userAuthenticated) throws IOException, MaxUploadSizeExceededException {
        this.usersService.imgUpload(img, userAuthenticated.getId());
    }

    //PUT CHANGE USER ROLE
    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRespDTO updateUserRole(@RequestBody UsersRoleDTO payload) {
        User user = this.usersService.updateUserRole(payload);
        return this.modelMapper.map(user, UserRespDTO.class);
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
    public Page<UserRespDTO> findByName(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size,
                                        @RequestParam(defaultValue = "surname") String sortBy,
                                        @RequestParam String user
    ) {
        Page<User> userPage = this.usersService.findByName(page, size, sortBy, user);
        return userPage.map(userEntity -> modelMapper.map(userEntity, UserRespDTO.class));
    }

    //PUT ME PASSWORD
    @PutMapping("/me/password")
    public ResetPasswordRespDTO updateMyPassword(@AuthenticationPrincipal User userAuthenticated, @RequestBody ChangeUserPasswordDTO payload) {
        this.usersService.changeUserPassword(userAuthenticated.getId(), payload);
        return new ResetPasswordRespDTO("Password changed");
    }
}
