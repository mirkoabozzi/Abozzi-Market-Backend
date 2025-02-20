package mirkoabozzi.Abozzi.Market.dto.response;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime timestamp) {
}
