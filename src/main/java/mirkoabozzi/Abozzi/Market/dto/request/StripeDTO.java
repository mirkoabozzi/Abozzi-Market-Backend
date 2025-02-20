package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StripeDTO(
        @NotNull(message = "Sum is required. ")
        Double sum,
        @NotEmpty(message = "Approved Url is required. ")
        String approvedUrl,
        @NotEmpty(message = "Failed Url is required. ")
        String failedUrl
) {

}
