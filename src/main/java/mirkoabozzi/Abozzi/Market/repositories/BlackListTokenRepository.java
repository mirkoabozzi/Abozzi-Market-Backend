package mirkoabozzi.Abozzi.Market.repositories;

import mirkoabozzi.Abozzi.Market.entities.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListToken, UUID> {
    Optional<BlackListToken> findByToken(String token);

    void deleteByCreatedAtBefore(LocalDateTime targetDate);
}
