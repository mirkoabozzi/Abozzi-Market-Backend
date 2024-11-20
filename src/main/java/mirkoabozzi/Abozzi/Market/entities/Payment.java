package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor
public abstract class Payment {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private LocalDateTime paymentDate;
    private Double total;
    private String status;
    private String description;

    public Payment(LocalDateTime paymentDate, Double total, String status, String description) {
        this.paymentDate = paymentDate;
        this.total = total;
        this.status = status;
        this.description = description;
    }
}
