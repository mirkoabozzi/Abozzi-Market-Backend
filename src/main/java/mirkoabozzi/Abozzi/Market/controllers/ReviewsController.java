package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import mirkoabozzi.Abozzi.Market.dto.request.ReviewsDTO;
import mirkoabozzi.Abozzi.Market.dto.request.ReviewsUpdateDTO;
import mirkoabozzi.Abozzi.Market.dto.response.ReviewRespDTO;
import mirkoabozzi.Abozzi.Market.entities.Review;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.ReviewsService;
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
@RequestMapping("/reviews")
@Tag(name = "Reviews")
public class ReviewsController {
    @Autowired
    private ReviewsService reviewsService;
    @Autowired
    private ModelMapper modelMapper;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewRespDTO addReview(@RequestBody @Validated ReviewsDTO payload, @AuthenticationPrincipal User userAuthenticated, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Review review = this.reviewsService.addReview(payload, userAuthenticated.getId());
            return this.modelMapper.map(review, ReviewRespDTO.class);
        }
    }

    //GET MY REVIEWS
    @GetMapping("/me")
    public Page<ReviewRespDTO> getMyReviews(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size,
                                            @RequestParam(defaultValue = "product") String sortBy,
                                            @AuthenticationPrincipal User userAuthenticated
    ) {
        Page<Review> reviewPage = this.reviewsService.getMyReviews(page, size, sortBy, userAuthenticated.getId());
        return reviewPage.map(review -> this.modelMapper.map(review, ReviewRespDTO.class));
    }

    //GET REVIEWS BY PRODUCT ID
    @GetMapping("/product/{id}")
    public Page<ReviewRespDTO> getReviewsByProductId(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size,
                                                     @RequestParam(defaultValue = "publishDate") String sortBy,
                                                     @PathVariable UUID id
    ) {
        Page<Review> reviewPage = this.reviewsService.getReviewsByProductId(page, size, sortBy, id);
        return reviewPage.map(review -> this.modelMapper.map(review, ReviewRespDTO.class));
    }

    //PUT UPDATE MY REVIEWS
    @PutMapping("/me/{id}")
    public ReviewRespDTO updateReview(@PathVariable UUID id, @AuthenticationPrincipal User userAuthenticated, @RequestBody @Validated ReviewsUpdateDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Review review = this.reviewsService.updateReview(id, payload, userAuthenticated.getId());
            return this.modelMapper.map(review, ReviewRespDTO.class);
        }
    }

    //DELETE
    @DeleteMapping("/me/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyReview(@PathVariable UUID id, @AuthenticationPrincipal User userAuthenticated) {
        this.reviewsService.removeMyReview(id, userAuthenticated.getId());
    }
}
