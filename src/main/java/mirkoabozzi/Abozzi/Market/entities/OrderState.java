package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "orders_state")
@Getter
@Setter
@NoArgsConstructor
public class OrderState {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String name;

    public OrderState(String name) {
        this.name = name;
    }
}
