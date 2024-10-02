package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.ReviewsDTO;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.entities.Review;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ReviewsService {
    @Autowired
    private ReviewsRepository reviewsRepository;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private UsersService usersService;

    //ADD REVIEWS
    public Review addReview(ReviewsDTO payload, UUID authenticatedUserId) {
        if (reviewsRepository.existsByUserIdAndProductId(authenticatedUserId, UUID.fromString(payload.product())))
            throw new BadRequestException("You already have a review for this product!");
        User userFound = this.usersService.findById(authenticatedUserId);
        Product productFound = this.productsService.findById(UUID.fromString(payload.product()));
        Review newReview = new Review(payload.rating(), payload.comment(), LocalDate.now(), LocalDate.now(), userFound, productFound);
        return this.reviewsRepository.save(newReview);
    }

    //FIND BY ID
    public Review findById(UUID id) {
        return this.reviewsRepository.findById(id).orElseThrow(() -> new NotFoundException("Review with id " + id + " not found on DB"));
    }

    //GET MY REVIEWS
    public Page<Review> getMyReviews(int page, int size, String sortBy, UUID id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.reviewsRepository.findByUserId(pageable, id);
    }

    //UPDATE
    public Review updateReview(UUID id, ReviewsDTO payload) {
        Review reviewFound = this.findById(id);
        reviewFound.setComment(payload.comment());
        reviewFound.setRating(payload.rating());
        reviewFound.setUpdatedAt(LocalDate.now());
        return this.reviewsRepository.save(reviewFound);
    }

    //REMOVE PRODUCT FROM MY WISHLIST
    public void removeMyReview(UUID id, UUID userId) {
        this.reviewsRepository.delete(this.reviewsRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Review with id " + id + " not found on DB")));
    }

}
