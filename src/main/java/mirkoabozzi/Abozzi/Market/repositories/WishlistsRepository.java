package mirkoabozzi.Abozzi.Market.repositories;

import mirkoabozzi.Abozzi.Market.entities.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistsRepository extends JpaRepository<Wishlist, UUID> {

    boolean existsByUserIdAndProductId(UUID userId, UUID productId);

    Page<Wishlist> findByUserId(Pageable pageable, UUID userId);

    Optional<Wishlist> findByProductIdAndUserId(UUID productId, UUID userId);
}
