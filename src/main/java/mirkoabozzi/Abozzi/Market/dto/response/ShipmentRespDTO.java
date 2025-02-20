package mirkoabozzi.Abozzi.Market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mirkoabozzi.Abozzi.Market.entities.User;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRespDTO {
    private UUID id;
    private String address;
    private int number;
    private String city;
    private String zipCode;
    private User user;
}
