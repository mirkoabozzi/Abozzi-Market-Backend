package mirkoabozzi.Abozzi.Market.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private String imgUrl;
    private boolean discountStatus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name = "products_discounts",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private List<Discount> discountList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
//    @JsonIgnore
    private List<Review> reviewList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<OrderDetail> orderDetailsList;

    public Product(String name, String description, double price, long quantityAvailable, LocalDateTime lastUpdate, String imgUrl, boolean discountStatus, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.createdAt = LocalDateTime.now();
        this.lastUpdate = lastUpdate;
        this.imgUrl = imgUrl;
        this.discountStatus = discountStatus;
        this.category = category;
        this.discountList = new ArrayList<>();
        this.reviewList = new ArrayList<>();
        this.orderDetailsList = new ArrayList<>();
    }
}
