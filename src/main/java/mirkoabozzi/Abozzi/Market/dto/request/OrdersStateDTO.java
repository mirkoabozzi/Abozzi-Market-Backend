package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record OrdersStateDTO(
        @NotEmpty(message = "Order is required. ")
        String order,
        @NotEmpty(message = "State is required. ")
        String state
) {
}
