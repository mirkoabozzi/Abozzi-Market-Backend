package mirkoabozzi.Abozzi.Market.repositories;

import mirkoabozzi.Abozzi.Market.entities.Discount;
import mirkoabozzi.Abozzi.Market.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductsRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);

    Page<Product> findByNameContainingIgnoreCase(Pageable pageable, String name);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE LOWER(c.name) = LOWER(:name)")
    Page<Product> findProductsByCategoryNameContaining(Pageable pageable, @Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    Page<Product> findByPriceRange(Pageable pageable, @Param("min") int min, @Param("max") int max);

    @Query("SELECT p FROM Product p WHERE p.discountStatus = true")
    Page<Product> findByDiscountStatus(Pageable pageable);

    List<Product> findByDiscountListContaining(Discount discount);
}
