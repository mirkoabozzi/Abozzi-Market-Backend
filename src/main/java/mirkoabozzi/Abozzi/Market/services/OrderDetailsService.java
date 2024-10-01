package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.entities.OrderDetail;
import mirkoabozzi.Abozzi.Market.repositories.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    //SAVE ORDER DETAILS
    public void saveAllOrderDetails(List<OrderDetail> orderDetailList) {
        this.orderDetailsRepository.saveAll(orderDetailList);
    }
}
