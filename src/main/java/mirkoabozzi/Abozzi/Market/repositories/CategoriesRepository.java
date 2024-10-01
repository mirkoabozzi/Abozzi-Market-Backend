package mirkoabozzi.Abozzi.Market.repositories;

import mirkoabozzi.Abozzi.Market.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
