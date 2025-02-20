package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DiscountsDTO(
        @NotEmpty(message = "Description is required. ")
        String description,
        @NotNull(message = "Percentage is required. ")
        double percentage,
        @NotNull(message = "Start date is required. ")
        LocalDateTime startDate,
        @NotNull(message = "End date is required. ")
        LocalDateTime endDate
) {
}
