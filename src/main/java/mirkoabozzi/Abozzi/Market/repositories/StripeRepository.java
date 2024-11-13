package mirkoabozzi.Abozzi.Market.repositories;

import mirkoabozzi.Abozzi.Market.entities.Stripe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StripeRepository extends JpaRepository<Stripe, UUID> {
    Optional<Stripe> findBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);

    List<Stripe> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
