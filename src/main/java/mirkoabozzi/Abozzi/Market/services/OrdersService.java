package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.OrdersDTO;
import mirkoabozzi.Abozzi.Market.entities.*;
import mirkoabozzi.Abozzi.Market.enums.OrdersState;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.OrdersRepository;
import mirkoabozzi.Abozzi.Market.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class OrdersService {
    @Autowired
    ProductsService productsService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private PaymentsService paymentsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ShipmentsService shipmentsService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private MailgunSender mailgunSender;
    @Autowired
    private PayPalService payPalService;

    //POST SAVE ORDER
    public Order saveOrder(OrdersDTO payload) {
        PayPal paymentFound = this.payPalService.findById(payload.payment());
        User userFound = this.usersService.findById(UUID.fromString(payload.user()));
        Shipment shipmentFound = this.shipmentsService.findById(UUID.fromString(payload.shipment()));
        Order newOrder = new Order(LocalDate.now(), OrdersState.PROCESSING, userFound, null, shipmentFound);
        List<OrderDetail> orderDetails = payload.orderDetails().stream().map(detailDTO -> {
            Product product = this.productsService.findById(UUID.fromString(detailDTO.product()));
            if (product.getQuantityAvailable() < detailDTO.quantity())
                throw new BadRequestException("Not enough stock for product " + product.getId());
            product.setQuantityAvailable(product.getQuantityAvailable() - detailDTO.quantity());
            return new OrderDetail(detailDTO.quantity(), newOrder, product);
        }).toList();
        newOrder.setPayment(paymentFound);
        Order savedOrder = this.ordersRepository.save(newOrder);
        this.orderDetailsService.saveAllOrderDetails(orderDetails);
//        this.mailgunSender.sendOrderCreatedEmail(userFound);
        return savedOrder;
    }

    public Order findById(UUID id) {
        return this.ordersRepository.findById(id).orElseThrow(() -> new NotFoundException("Order whit ID " + id + " not found"));
    }

    //GET ALL
    public Page<Order> getAllOrders(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.ordersRepository.findAll(pageable);
    }

    //DELETE
    public void delete(UUID id) {
        this.ordersRepository.delete(this.findById(id));
    }

    //GET MY ORDERS
    public Page<Order> getMyOrders(int page, int size, String sortBy, UUID id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.ordersRepository.findByUserId(pageable, id);
    }

    //DELETE MY ORDER
    public void deleteMyOrder(UUID id, UUID userId) {
        this.ordersRepository.delete(this.ordersRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Order whit ID " + id + " not found")));
    }
}
