package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Review(Double rating, User user, Product product) {
        this.rating = rating;

        this.user = user;
        this.product = product;
    }
}
