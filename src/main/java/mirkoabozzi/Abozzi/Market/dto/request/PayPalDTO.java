package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PayPalDTO(
        @NotNull(message = "Sum is required. ")
        Double sum,
        @NotEmpty(message = "Currency is required. ")
        String currency,
        @NotEmpty(message = "Method is required. ")
        String method,
        @NotEmpty(message = "Intent is required. ")
        String intent,
        @NotEmpty(message = "Description is required. ")
        String description,
        @NotEmpty(message = "Cancel URL is required. ")
        String cancelUrl,
        @NotEmpty(message = "Success URL is required. ")
        String successUrl
) {
}
