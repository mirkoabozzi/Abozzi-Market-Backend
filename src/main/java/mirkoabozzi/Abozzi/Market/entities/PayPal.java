package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paypal_payment")
@Getter
@Setter
@NoArgsConstructor
public class PayPal extends Payment {
    private String payerId;
    private String paymentId;

    public PayPal(String payerId, String paymentId) {
        this.payerId = payerId;
        this.paymentId = paymentId;
    }
}