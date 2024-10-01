package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PaymentsDTO(
        @NotEmpty(message = "Description is required. ")
        String description,
        @NotNull(message = "Total is required. ")
        Double total
) {
}
