package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mirkoabozzi.Abozzi.Market.enums.OrdersState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrdersState ordersState;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private PayPal payment;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetailList;


    public Order(LocalDateTime orderDate, OrdersState ordersState, User user, PayPal payment, Shipment shipment) {
        this.orderDate = orderDate;
        this.ordersState = ordersState;
        this.user = user;
        this.payment = payment;
        this.shipment = shipment;
        this.orderDetailList = new ArrayList<>();
    }
}

