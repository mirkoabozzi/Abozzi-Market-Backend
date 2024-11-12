package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stripe_payment")
@Getter
@Setter
@NoArgsConstructor
public class Stripe {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String sessionId;
    private String paymentIntentId;
    private Double amount;
    private String currency;
    private String status;
    private LocalDateTime paymentDate;


    public Stripe(String sessionId, String paymentIntentId, Double amount, String currency, String status, LocalDateTime paymentDate) {
        this.sessionId = sessionId;
        this.paymentIntentId = paymentIntentId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentDate = paymentDate;

    }
}
