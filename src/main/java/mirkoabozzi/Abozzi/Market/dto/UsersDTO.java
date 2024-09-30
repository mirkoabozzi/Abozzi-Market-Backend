package mirkoabozzi.Abozzi.Market.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UsersDTO(
        @NotEmpty(message = "Name is required. ")
        String name,
        @NotEmpty(message = "Surname is required. ")
        String surname,
        @Email(message = "Email is required. ")
        String email,
        @NotEmpty(message = "Password is required. ")
        String password,
        @NotEmpty(message = "Phone number is required. ")
        String phoneNumber
) {
}
