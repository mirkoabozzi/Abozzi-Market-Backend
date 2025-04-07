package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "black_list_tokens")
@Getter
@Setter
@NoArgsConstructor
public class BlackListToken {
    @Id
    @GeneratedValue
    private UUID id;
    private String token;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public BlackListToken(String token, LocalDateTime createdAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.user = user;
    }
}
