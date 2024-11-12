package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stripe_payment")
@Getter
@Setter
@NoArgsConstructor
public class Stripe extends Payment {
    private String sessionId;
    private String paymentIntentId;
    private String currency;

    public Stripe(String sessionId, String paymentIntentId, String currency) {
        this.sessionId = sessionId;
        this.paymentIntentId = paymentIntentId;
        this.currency = currency;
    }
}
