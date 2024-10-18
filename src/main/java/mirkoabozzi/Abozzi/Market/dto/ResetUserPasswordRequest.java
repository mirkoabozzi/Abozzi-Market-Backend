package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;

public record ResetUserPasswordRequest(
        @NotEmpty(message = "Password is required.")
        String email
) {
}
