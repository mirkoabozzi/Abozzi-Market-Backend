package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.NotEmpty;

public record UsersRoleDTO(
        @NotEmpty(message = "User is required. ")
        String user,
        @NotEmpty(message = "Role is required. ")
        String role
) {
}
