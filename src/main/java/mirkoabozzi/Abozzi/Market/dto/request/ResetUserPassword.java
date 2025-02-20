package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ResetUserPassword(
        @NotEmpty(message = "Password is required. ")
        String password
) {
}
