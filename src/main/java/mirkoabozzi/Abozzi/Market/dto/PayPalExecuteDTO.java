package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;

public record PayPalExecuteDTO(
        @NotEmpty(message = "Payment id is required. ")
        String paymentId,
        @NotEmpty(message = "Payer id is required. ")
        String payerId,
        @NotEmpty(message = "Approved Url is required. ")
        String approvedUrl,
        @NotEmpty(message = "Failed Url is required. ")
        String failedUrl
) {
}
