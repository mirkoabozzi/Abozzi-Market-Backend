package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "orders_state_id")
    private OrderState orderState;

    public Order(User user, Payment payment, Shipment shipment, OrderState orderState) {
        this.orderDate = LocalDate.now();
        this.user = user;
        this.payment = payment;
        this.shipment = shipment;
        this.orderState = orderState;
    }
}

