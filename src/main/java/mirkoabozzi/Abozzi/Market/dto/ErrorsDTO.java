package mirkoabozzi.Abozzi.Market.dto;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime timestamp) {
}
