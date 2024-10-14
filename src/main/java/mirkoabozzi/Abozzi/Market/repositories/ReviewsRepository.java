package mirkoabozzi.Abozzi.Market.repositories;

import mirkoabozzi.Abozzi.Market.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, UUID> {
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);

    Page<Review> findByUserId(Pageable pageable, UUID userId);

    Page<Review> findByProductId(Pageable pageable, UUID productId);
}
