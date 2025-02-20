package mirkoabozzi.Abozzi.Market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mirkoabozzi.Abozzi.Market.entities.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRespDTO {
    private UUID id;
    private int rating;
    private String comment;
    private LocalDateTime publishDate;
    private LocalDateTime updatedAt;
    private User user;
}
