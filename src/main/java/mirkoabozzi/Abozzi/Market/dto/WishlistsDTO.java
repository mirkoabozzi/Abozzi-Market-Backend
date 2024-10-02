package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;

public record WishlistsDTO(
        @NotEmpty(message = "Product is required. ")
        String product
) {
}
