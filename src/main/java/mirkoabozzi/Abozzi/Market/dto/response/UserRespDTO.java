package mirkoabozzi.Abozzi.Market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mirkoabozzi.Abozzi.Market.enums.RegistrationMethod;
import mirkoabozzi.Abozzi.Market.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDTO {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private LocalDateTime registrationDate;
    private Role role;
    private String avatar;
    private Boolean isVerified;
    private RegistrationMethod registrationMethod;
}
