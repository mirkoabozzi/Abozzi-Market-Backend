package mirkoabozzi.Abozzi.Market.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record MailDTO(
        @NotEmpty(message = "Name is required. ")
        String name,
        @NotEmpty(message = "Email is required. ")
        String email,
        @NotEmpty(message = "Text is required. ")
        String text
) {
}
