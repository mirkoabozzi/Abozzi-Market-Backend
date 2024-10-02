package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReviewsDTO(
        @NotNull(message = "Raring is required. ")
        @Min(value = 1, message = "Rating should not be less than 1 ")
        @Max(value = 5, message = "Rating should not be greater than 5 ")
        int rating,
        @NotEmpty(message = "Comment is required. ")
        String comment,
        @NotEmpty(message = "Product is required. ")
        String product
) {
}
