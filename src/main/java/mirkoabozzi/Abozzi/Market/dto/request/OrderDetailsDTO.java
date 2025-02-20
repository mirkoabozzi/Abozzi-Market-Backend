package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderDetailsDTO(
        @NotEmpty(message = "Product is required. ")
        String product,
        @NotNull(message = "Quantity is required. ")
        int quantity,
        @NotNull(message = "Price is required. ")
        double price
) {
}
