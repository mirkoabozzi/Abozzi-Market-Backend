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
    private String description;
    private String payerId;
    private String paymentId;

    public PayPal(String description, String payerId, String paymentId) {
        this.description = description;
        this.payerId = payerId;
        this.paymentId = paymentId;
    }
}