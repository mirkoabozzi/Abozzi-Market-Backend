package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ShipmentsDTO(
        @NotEmpty(message = "Address is required. ")
        String address,
        @NotNull(message = "Number is required. ")
        int number,
        @NotEmpty(message = "City is required. ")
        String city,
        @NotEmpty(message = "Zip code is required. ")
        String zipCode
) {
}
