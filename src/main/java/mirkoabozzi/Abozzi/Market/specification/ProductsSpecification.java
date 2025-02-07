package mirkoabozzi.Abozzi.Market.specification;

import mirkoabozzi.Abozzi.Market.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductsSpecification {

    public static Specification<Product> hasName(String name) {
        return ((root, query, criteriaBuilder) -> {
            if (name == null) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        });

    }

    public static Specification<Product> hasCategoryName(String categoryName) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryName == null) return null;
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("categoryName")), categoryName.toLowerCase());
        });
    }

    public static Specification<Product> hasPriceRange(Double min, Double max) {
        return ((root, query, criteriaBuilder) -> {
            if (min == null || max == null) return null;
            return criteriaBuilder.between(root.get("price"), min, max);
        });
    }

    public static Specification<Product> hasDiscount(Boolean discountStatus) {
        return ((root, query, criteriaBuilder) -> {
            if (discountStatus == null) return null;
            return criteriaBuilder.equal(root.get("discountStatus"), discountStatus);
        });
    }
}
