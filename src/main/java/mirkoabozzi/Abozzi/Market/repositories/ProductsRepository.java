package mirkoabozzi.Abozzi.Market.repositories;

import mirkoabozzi.Abozzi.Market.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductsRepository extends JpaRepository<Product, UUID> {
    boolean existsByName(String name);
}
