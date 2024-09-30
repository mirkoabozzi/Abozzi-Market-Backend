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
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String address;
    private int number;
    private String city;
    private String zipCode;

    public Shipment(String address, int number, String city, String zipCode) {
        this.address = address;
        this.number = number;
        this.city = city;
        this.zipCode = zipCode;
    }
}
