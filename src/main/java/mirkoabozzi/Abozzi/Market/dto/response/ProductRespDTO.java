package mirkoabozzi.Abozzi.Market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mirkoabozzi.Abozzi.Market.entities.Category;
import mirkoabozzi.Abozzi.Market.entities.Discount;
import mirkoabozzi.Abozzi.Market.entities.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRespDTO {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private long quantityAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private String imgUrl;
    private boolean discountStatus;
    private Category category;
    private List<Discount> discountList;
    private List<Review> reviewList;
}
