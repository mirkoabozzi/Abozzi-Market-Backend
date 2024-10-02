package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.WishlistsDTO;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.entities.Wishlist;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.WishlistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WishlistsService {
    @Autowired
    private WishlistsRepository wishlistsRepository;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private UsersService usersService;

    //SAVE ADD TO WISHLIST
    public Wishlist addToWishList(WishlistsDTO payload, UUID authenticatedUserId) {
        if (wishlistsRepository.existsByUserIdAndProductId(authenticatedUserId, UUID.fromString(payload.product())))
            throw new BadRequestException("Product " + payload.product() + " already on your wishlist");
        User userFound = this.usersService.findById(authenticatedUserId);
        Product productFound = this.productsService.findById(UUID.fromString(payload.product()));
        Wishlist newWishlist = new Wishlist(userFound, productFound);
        return this.wishlistsRepository.save(newWishlist);
    }

    //FIND BY ID
    public Wishlist findById(UUID id) {
        return this.wishlistsRepository.findById(id).orElseThrow(() -> new NotFoundException("Wishlist with id " + id + " not found on DB"));
    }

    //GET MY ORDERS
    public Page<Wishlist> getMyWishlist(int page, int size, String sortBy, UUID id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.wishlistsRepository.findByUserId(pageable, id);
    }

    //REMOVE PRODUCT FROM MY WISHLIST
    public void removeFromWishlist(UUID id, UUID userId) {
        this.wishlistsRepository.delete(this.wishlistsRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Wishlist with id " + id + " not found on DB")));
    }
}
