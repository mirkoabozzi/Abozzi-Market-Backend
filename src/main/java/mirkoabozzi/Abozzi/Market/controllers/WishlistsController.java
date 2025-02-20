package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import mirkoabozzi.Abozzi.Market.dto.request.WishlistsDTO;
import mirkoabozzi.Abozzi.Market.dto.response.WishlistRespDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.entities.Wishlist;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.WishlistsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wishlists")
@Tag(name = "Wishlists")
public class WishlistsController {
    @Autowired
    private WishlistsService wishlistsService;
    @Autowired
    private ModelMapper modelMapper;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistRespDTO addToWishlist(@RequestBody @Validated WishlistsDTO payload, @AuthenticationPrincipal User userAuthenticated, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Wishlist wishlist = this.wishlistsService.addToWishList(payload, userAuthenticated.getId());
            return this.modelMapper.map(wishlist, WishlistRespDTO.class);
        }
    }

    //GET MY WISHLIST
    @GetMapping("/me")
    public Page<WishlistRespDTO> getMyWishlist(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "24") int size,
                                               @RequestParam(defaultValue = "product") String sortBy,
                                               @AuthenticationPrincipal User userAuthenticated
    ) {
        Page<Wishlist> wishlistPage = this.wishlistsService.getMyWishlist(page, size, sortBy, userAuthenticated.getId());
        return wishlistPage.map(wishlist -> this.modelMapper.map(wishlist, WishlistRespDTO.class));
    }

    //DELETE
    @DeleteMapping("/me/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromWishlist(@PathVariable UUID id, @AuthenticationPrincipal User userAuthenticated) {
        this.wishlistsService.removeFromWishlist(id, userAuthenticated.getId());
    }
}
