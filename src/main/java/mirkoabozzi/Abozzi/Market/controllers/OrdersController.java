package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import mirkoabozzi.Abozzi.Market.dto.request.OrdersDTO;
import mirkoabozzi.Abozzi.Market.dto.request.OrdersStateDTO;
import mirkoabozzi.Abozzi.Market.dto.response.OrderRespDTO;
import mirkoabozzi.Abozzi.Market.entities.Order;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.OrdersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ModelMapper modelMapper;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public OrderRespDTO saveOrder(@RequestBody @Validated OrdersDTO payload, BindingResult validation) throws MessagingException {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Order order = this.ordersService.saveOrder(payload);
            return this.modelMapper.map(order, OrderRespDTO.class);
        }
    }

    //GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<OrderRespDTO> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "orderDate") String sortBy
    ) {
        Page<Order> orderPage = this.ordersService.getAllOrders(page, size, sortBy);
        return orderPage.map(order -> modelMapper.map(order, OrderRespDTO.class));
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteOrder(@PathVariable UUID id) {
        this.ordersService.delete(id);
    }

    //GET MY ORDER
    @GetMapping("/me")
    public Page<OrderRespDTO> getMyOrders(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "orderDate") String sortBy,
                                          @AuthenticationPrincipal User userAuthenticated
    ) {
        Page<Order> orderPage = this.ordersService.getMyOrders(page, size, sortBy, userAuthenticated.getId());
        return orderPage.map(order -> modelMapper.map(order, OrderRespDTO.class));
    }

    //DELETE
    @DeleteMapping("/me/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyOrder(@PathVariable UUID id, @AuthenticationPrincipal User userAuthenticated) {
        this.ordersService.deleteMyOrder(id, userAuthenticated.getId());
    }

    //GET MY ORDER BY ID
    @GetMapping("/me/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public OrderRespDTO getMyOrderById(@PathVariable UUID id, @AuthenticationPrincipal User userAuthenticated) {
        Order order = this.ordersService.findMyOrderById(id, userAuthenticated.getId());
        return this.modelMapper.map(order, OrderRespDTO.class);
    }

    //UPDATE ORDER STATE
    @PutMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderRespDTO updateOrder(@RequestBody @Validated OrdersStateDTO payload, BindingResult validation) throws MessagingException {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Order order = this.ordersService.updateOrderState(payload);
            return this.modelMapper.map(order, OrderRespDTO.class);
        }
    }

    // GET BY USER EMAIL
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<OrderRespDTO> getOrderByUserEmail(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "orderDate") String sortBy,
                                                  @RequestParam String email
    ) {
        Page<Order> orderPage = this.ordersService.findByUserEmail(page, size, sortBy, email);
        return orderPage.map(order -> modelMapper.map(order, OrderRespDTO.class));
    }

    //GET ORDER BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderRespDTO getOrderById(@PathVariable UUID id) {
        Order order = this.ordersService.findById(id);
        return this.modelMapper.map(order, OrderRespDTO.class);
    }
}