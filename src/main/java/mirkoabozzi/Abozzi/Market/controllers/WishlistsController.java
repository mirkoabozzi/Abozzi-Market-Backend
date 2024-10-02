package mirkoabozzi.Abozzi.Market.controllers;

import mirkoabozzi.Abozzi.Market.dto.WishlistsDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.entities.Wishlist;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.WishlistsService;
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
public class WishlistsController {
    @Autowired
    private WishlistsService wishlistsService;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Wishlist addToWishlist(@RequestBody @Validated WishlistsDTO payload, @AuthenticationPrincipal User userAuthenticated, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.wishlistsService.addToWishList(payload, userAuthenticated.getId());
        }
    }

    //GET MY WISHLIST
    @GetMapping("/me")
    public Page<Wishlist> getMyWishlist(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "product") String sortBy,
                                        @AuthenticationPrincipal User userAuthenticated
    ) {
        return this.wishlistsService.getMyWishlist(page, size, sortBy, userAuthenticated.getId());
    }

    //DELETE
    @DeleteMapping("/me/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromWishlist(@PathVariable UUID id, @AuthenticationPrincipal User userAuthenticated) {
        this.wishlistsService.removeFromWishlist(id, userAuthenticated.getId());
    }
}
