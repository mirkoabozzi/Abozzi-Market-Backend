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
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String description;
    private double percentage;
    private LocalDate startDate;
    private LocalDate endDate;

    public Discount(String description, double percentage, LocalDate startDate, LocalDate endDate) {
        this.description = description;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
