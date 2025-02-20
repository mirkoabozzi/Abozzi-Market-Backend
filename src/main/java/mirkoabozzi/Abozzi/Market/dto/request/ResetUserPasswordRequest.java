package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ResetUserPasswordRequest(
        @NotEmpty(message = "Email is required.")
        String email
) {
}
