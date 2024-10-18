package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;

public record ResetUserPassword(
        @NotEmpty(message = "Password is required. ")
        String password
) {
}
