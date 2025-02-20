package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductsDTO(
        @NotEmpty(message = "Name is required. ")
        String name,
        @NotEmpty(message = "Description is required. ")
        String description,
        @NotNull(message = "Price is required. ")
        double price,
        @NotNull(message = "Quantity is required. ")
        long quantityAvailable,
        @NotEmpty(message = "Category ID is required. ")
        String category
) {
}
