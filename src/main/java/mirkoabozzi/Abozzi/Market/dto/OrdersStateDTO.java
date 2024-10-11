package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;

public record OrdersStateDTO(
        @NotEmpty(message = "Order is required. ")
        String order,
        @NotEmpty(message = "State is required. ")
        String state
) {
}
