package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DiscountsDTO(
        @NotEmpty(message = "Description is required. ")
        String description,
        @NotNull(message = "Percentage is required. ")
        double percentage,
        @NotNull(message = "Start date is required. ")
        LocalDate startDate,
        @NotNull(message = "End date is required. ")
        LocalDate endDate
) {
}
