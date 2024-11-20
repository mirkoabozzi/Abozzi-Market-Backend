package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "paypal_payment")
@Getter
@Setter
@NoArgsConstructor
public class PayPal extends Payment {
    private String payerId;
    private String paymentId;

    public PayPal(LocalDateTime paymentDate, Double total, String status, String description, String payerId, String paymentId) {
        super(paymentDate, total, status, description);
        this.payerId = payerId;
        this.paymentId = paymentId;
    }
}