package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ProductDiscountDTO(
        @NotEmpty(message = "Discount is required. ")
        String discount
) {
}
