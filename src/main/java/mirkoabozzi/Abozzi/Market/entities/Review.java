package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private Double rating;
    private String comment;
    private LocalDate publishDate;
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Review(Double rating, String comment, LocalDate publishDate, LocalDate updatedAt, User user, Product product) {
        this.rating = rating;
        this.comment = comment;
        this.publishDate = publishDate;
        this.updatedAt = updatedAt;
        this.user = user;
        this.product = product;
    }
}
