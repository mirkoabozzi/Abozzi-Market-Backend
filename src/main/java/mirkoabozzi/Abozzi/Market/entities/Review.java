package mirkoabozzi.Abozzi.Market.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private int rating;
    private String comment;
    private LocalDateTime publishDate;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;

    public Review(int rating, String comment, LocalDateTime publishDate, LocalDateTime updatedAt, User user, Product product) {
        this.rating = rating;
        this.comment = comment;
        this.publishDate = publishDate;
        this.updatedAt = updatedAt;
        this.user = user;
        this.product = product;
    }
}
