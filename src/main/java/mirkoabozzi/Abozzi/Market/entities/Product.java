package mirkoabozzi.Abozzi.Market.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String name;
    private String description;
    private double price;
    private long quantityAvailable;
    private LocalDate createdAt;
    private LocalDate lastUpdate;
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name = "products_discounts",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private List<Discount> discountList;

    public Product(String name, String description, double price, long quantityAvailable, LocalDate lastUpdate, String imgUrl, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.createdAt = LocalDate.now();
        this.lastUpdate = lastUpdate;
        this.imgUrl = imgUrl;
        this.category = category;
        this.discountList= new ArrayList<>();
    }
}
