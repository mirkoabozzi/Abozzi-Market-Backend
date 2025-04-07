package mirkoabozzi.Abozzi.Market.repositories;

import mirkoabozzi.Abozzi.Market.entities.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BlackListTokenRepository extends JpaRepository<BlackListToken, UUID> {
    Optional<BlackListToken> findByToken(String token);
}
