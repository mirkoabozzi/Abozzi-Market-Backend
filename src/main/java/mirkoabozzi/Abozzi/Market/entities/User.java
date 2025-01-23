package mirkoabozzi.Abozzi.Market.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mirkoabozzi.Abozzi.Market.enums.RegistrationMethod;
import mirkoabozzi.Abozzi.Market.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"password", "authorities", "enabled", "accountNonLocked", "credentialsNonExpired", "accountNonExpired", "username", "resetPasswordToken", "tokenDuration", "verificationToken"})
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
    private LocalDateTime registrationDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String avatar;
    private Boolean isVerified;
    private String verificationToken;
    private String resetPasswordToken;
    private LocalDateTime tokenDuration;
    @Enumerated(EnumType.STRING)
    private RegistrationMethod registrationMethod;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Shipment> shipments;

    public User(String name, String surname, String email, String password, String phoneNumber, String avatar, RegistrationMethod registrationMethod) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.registrationDate = LocalDateTime.now();
        this.role = Role.USER;
        this.avatar = avatar;
        this.isVerified = false;
        this.registrationMethod = registrationMethod;
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
