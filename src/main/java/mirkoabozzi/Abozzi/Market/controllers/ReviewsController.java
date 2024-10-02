package mirkoabozzi.Abozzi.Market.controllers;

import mirkoabozzi.Abozzi.Market.dto.ReviewsDTO;
import mirkoabozzi.Abozzi.Market.entities.Review;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.ReviewsService;
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
@RequestMapping("/reviews")
public class ReviewsController {
    @Autowired
    private ReviewsService reviewsService;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@RequestBody @Validated ReviewsDTO payload, @AuthenticationPrincipal User userAuthenticated, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.reviewsService.addReview(payload, userAuthenticated.getId());
        }
    }

    //GET MY REVIEWS
    @GetMapping("/me")
    public Page<Review> getMyReviews(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "product") String sortBy,
                                     @AuthenticationPrincipal User userAuthenticated
    ) {
        return this.reviewsService.getMyReviews(page, size, sortBy, userAuthenticated.getId());
    }

    //PUT UPDATE MY REVIEWS
    @PutMapping("/me/{id}")
    public Review updateReview(@PathVariable UUID id, @RequestBody @Validated ReviewsDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.reviewsService.updateReview(id, payload);
        }
    }

    //DELETE
    @DeleteMapping("/me/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyReview(@PathVariable UUID id, @AuthenticationPrincipal User userAuthenticated) {
        this.reviewsService.removeMyReview(id, userAuthenticated.getId());
    }
}