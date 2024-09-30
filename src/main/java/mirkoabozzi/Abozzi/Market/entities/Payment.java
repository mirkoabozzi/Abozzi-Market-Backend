package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String description;
    private Double total;
    private LocalDate paymentDate;

    public Payment(String description, Double total) {
        this.description = description;
        this.total = total;
        this.paymentDate = LocalDate.now();
    }
}
