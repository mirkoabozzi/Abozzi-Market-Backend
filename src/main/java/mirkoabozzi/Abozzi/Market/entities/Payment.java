package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "payment_type")
@Getter
@Setter
@NoArgsConstructor
public abstract class Payment {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime paymentDate;
    private Double total;
    private String status;
    private String description;
}
