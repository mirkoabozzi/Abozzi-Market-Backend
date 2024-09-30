package mirkoabozzi.Abozzi.Market.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mirkoabozzi.Abozzi.Market.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"password", "authorities", "enabled", "accountNonLocked", "credentialsNonExpired", "accountNonExpired", "username"})
public class User implements UserDetails {
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
    @Enumerated(EnumType.STRING)
    private Role role;
    private String avatar;

    public User(String name, String surname, String email, String password, String phoneNumber, String avatar) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.registrationDate = LocalDate.now();
        this.role = Role.USER;
        this.avatar = avatar;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
