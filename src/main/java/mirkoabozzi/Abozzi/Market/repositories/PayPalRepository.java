package mirkoabozzi.Abozzi.Market.repositories;

import mirkoabozzi.Abozzi.Market.entities.PayPal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayPalRepository extends JpaRepository<PayPal, UUID> {

    Optional<PayPal> findByPaymentId(String paymentId);
}
