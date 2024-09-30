package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate registrationDate;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User(String name, String surname, String email, String password, String phoneNumber, Role role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.registrationDate = LocalDate.now();
        this.role = role;
    }
}
