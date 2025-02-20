package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CategoriesDTO(
        @NotEmpty(message = "Name is required. ")
        String name
) {
}
