package mirkoabozzi.Abozzi.Market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mirkoabozzi.Abozzi.Market.entities.OrderDetail;
import mirkoabozzi.Abozzi.Market.entities.Payment;
import mirkoabozzi.Abozzi.Market.entities.Shipment;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.enums.OrdersState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRespDTO {
    private UUID id;
    private LocalDateTime orderDate;
    private OrdersState ordersState;
    private User user;
    private Payment payment;
    private Shipment shipment;
    private List<OrderDetail> orderDetailList;
}
