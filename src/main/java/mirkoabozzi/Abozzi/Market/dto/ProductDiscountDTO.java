package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;

public record ProductDiscountDTO(
        @NotEmpty(message = "Discount is required. ")
        String discount
) {
}
