package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ChangeUserPasswordDTO(
        @NotEmpty(message = "Old password is required. ")
        String oldPassword,
        @NotEmpty(message = "New password is required. ")
        String newPassword
) {
}
