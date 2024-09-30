package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UsersLoginDTO(
        @Email(message = "Email is required. ")
        String email,
        @NotNull(message = "Password is required. ")
        String password
) {
}
