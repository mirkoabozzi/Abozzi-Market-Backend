package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.request.ReviewsDTO;
import mirkoabozzi.Abozzi.Market.dto.request.ReviewsUpdateDTO;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.entities.Review;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.exceptions.UnauthorizedException;
import mirkoabozzi.Abozzi.Market.repositories.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        Review newReview = new Review(payload.rating(), payload.comment(), LocalDateTime.now(), LocalDateTime.now(), userFound, productFound);
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

    //GET REVIEWS BY PRODUCT ID
    public Page<Review> getReviewsByProductId(int page, int size, String sortBy, UUID id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.reviewsRepository.findByProductId(pageable, id);
    }

    //UPDATE
    public Review updateReview(UUID id, ReviewsUpdateDTO payload, UUID authenticatedUserId) {
        Review reviewFound = this.findById(id);
        if (!reviewFound.getUser().getId().equals(authenticatedUserId))
            throw new UnauthorizedException("Unauthorized!");
        reviewFound.setComment(payload.comment());
        reviewFound.setRating(payload.rating());
        reviewFound.setUpdatedAt(LocalDateTime.now());
        return this.reviewsRepository.save(reviewFound);
    }

    //REMOVE PRODUCT FROM MY WISHLIST
    public void removeMyReview(UUID id, UUID userId) {
        Review reviewFound = this.findById(id);
        if (!reviewFound.getUser().getId().equals(userId)) throw new UnauthorizedException("Unauthorized!");
        this.reviewsRepository.delete(reviewFound);
    }
}
