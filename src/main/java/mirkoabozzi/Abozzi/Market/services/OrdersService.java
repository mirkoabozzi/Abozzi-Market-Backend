package mirkoabozzi.Abozzi.Market.services;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import mirkoabozzi.Abozzi.Market.dto.request.OrdersDTO;
import mirkoabozzi.Abozzi.Market.dto.request.OrdersStateDTO;
import mirkoabozzi.Abozzi.Market.entities.*;
import mirkoabozzi.Abozzi.Market.enums.OrdersState;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.exceptions.UnauthorizedException;
import mirkoabozzi.Abozzi.Market.repositories.OrdersRepository;
import mirkoabozzi.Abozzi.Market.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrdersService {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private OrdersRepository ordersRepository;
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
    @Autowired
    private StripeService stripeService;
    @Autowired
    private MailService mailService;

    //POST SAVE ORDER
    @Transactional
    public Order saveOrder(OrdersDTO payload) throws MessagingException {
        User userFound = this.usersService.findById(UUID.fromString(payload.user()));
        Order newOrder = new Order(LocalDateTime.now(), OrdersState.PROCESSING, userFound);

        if (payload.payment().startsWith("PAYID-")) {
            PayPal paypalPaymentFound = this.payPalService.findByPaymentId(payload.payment());
            newOrder.setPayment(paypalPaymentFound);
        } else if (payload.payment().startsWith("cs")) {
            Stripe stripePaymentFound = this.stripeService.findBySessionId(payload.payment());
            newOrder.setPayment(stripePaymentFound);
        }

        if (payload.shipment() != null) {
            Shipment shipmentFound = this.shipmentsService.findById(UUID.fromString(payload.shipment()));
            newOrder.setShipment(shipmentFound);
        }

        List<OrderDetail> orderDetails = payload.orderDetails().stream().map(detailDTO -> {
            Product product = this.productsService.findById(UUID.fromString(detailDTO.product()));
            if (product.getQuantityAvailable() < detailDTO.quantity())
                throw new BadRequestException("Not enough stock for product " + product.getId());
            product.setQuantityAvailable(product.getQuantityAvailable() - detailDTO.quantity());
            return new OrderDetail(detailDTO.quantity(), newOrder, product, detailDTO.price());
        }).toList();

        Order savedOrder = this.ordersRepository.save(newOrder);
        this.orderDetailsService.saveAllOrderDetails(orderDetails);
//        this.mailgunSender.sendOrderCreatedEmail(userFound);
        this.mailService.orderConfirmationEmail(userFound, newOrder, orderDetails);
        return savedOrder;
    }

    //FIND BY ID
    public Order findById(UUID id) {
        return this.ordersRepository.findById(id).orElseThrow(() -> new NotFoundException("Order whit ID " + id + " not found"));
    }

    //GET ALL
    public Page<Order> getAllOrders(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return this.ordersRepository.findAll(pageable);
    }

    //DELETE
    public void delete(UUID id) {
        this.ordersRepository.delete(this.findById(id));
    }

    //GET MY ORDERS
    public Page<Order> getMyOrders(int page, int size, String sortBy, UUID id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return this.ordersRepository.findByUserId(pageable, id);
    }

    //DELETE MY ORDER
    public void deleteMyOrder(UUID id, UUID userId) {
        this.ordersRepository.delete(this.ordersRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Order whit ID " + id + " not found")));
    }

    //GET MY ORDER
    public Order findMyOrderById(UUID id, UUID userId) {
        User userFound = this.usersService.findById(userId);
        Order orderFound = this.ordersRepository.findById(id).orElseThrow(() -> new NotFoundException("Order whit ID " + id + " not found"));
        if (userFound.getId() != orderFound.getUser().getId()) throw new UnauthorizedException("This isn't you order!");
        return orderFound;
    }

    //UPDATE ORDER STATE
    public Order updateOrderState(OrdersStateDTO payload) throws MessagingException {
        Order orderFound = this.findById(UUID.fromString(payload.order()));
        orderFound.setOrdersState(OrdersState.valueOf(payload.state()));
        Order orderSaved = this.ordersRepository.save(orderFound);
        this.mailService.orderStatusEmail(orderFound);
        return orderSaved;
    }

    //FIND BY USER EMAIL
    public Page<Order> findByUserEmail(int pages, int size, String sortBy, String email) {
        Pageable pageable = PageRequest.of(pages, size, Sort.by(sortBy).descending());
        return this.ordersRepository.findByUserEmail(pageable, email);
    }
}
