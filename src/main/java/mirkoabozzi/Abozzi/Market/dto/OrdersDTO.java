package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrdersDTO(
        @NotEmpty(message = "User is required. ")
        String user,
        @NotEmpty(message = "Payment is required. ")
        String payment,
        @NotEmpty(message = "Shipment is required. ")
        String shipment,
        @NotEmpty(message = "Order detail list is required. ")
        List<OrderDetailsDTO> orderDetails
) {
}
