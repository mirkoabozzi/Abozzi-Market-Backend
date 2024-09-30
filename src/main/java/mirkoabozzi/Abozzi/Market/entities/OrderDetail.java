package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "orders_details")
@Getter
@Setter
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderDetail(int quantity, Order order, Product product) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
    }
}
