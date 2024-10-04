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
@Table(name = "paypal_payment")
@Getter
@Setter
@NoArgsConstructor
public class PayPal {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String description;
    private Double total;
    private LocalDateTime paymentDate;
    private String payerId;
    private String paymentId;
    private String status;

    public PayPal(String description, Double total, LocalDateTime paymentDate, String payerId, String paymentId) {
        this.description = description;
        this.total = total;
        this.paymentDate = paymentDate;
        this.payerId = payerId;
        this.paymentId = paymentId;
    }
}