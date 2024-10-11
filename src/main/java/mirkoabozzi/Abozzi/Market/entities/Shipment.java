package mirkoabozzi.Abozzi.Market.entities;


import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Shipment(String address, int number, String city, String zipCode, User user) {
        this.address = address;
        this.number = number;
        this.city = city;
        this.zipCode = zipCode;
        this.user = user;
    }
}
