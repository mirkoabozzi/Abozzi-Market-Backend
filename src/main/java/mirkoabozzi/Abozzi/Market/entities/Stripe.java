package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stripe_payment")
@Getter
@Setter
@NoArgsConstructor
public class Stripe extends Payment {
    private String sessionId;
    private String paymentIntentId;
    private String currency;

    public Stripe(LocalDateTime paymentDate, Double total, String status, String description, String sessionId, String paymentIntentId, String currency) {
        super(paymentDate, total, status, description);
        this.sessionId = sessionId;
        this.paymentIntentId = paymentIntentId;
        this.currency = currency;
    }
}
