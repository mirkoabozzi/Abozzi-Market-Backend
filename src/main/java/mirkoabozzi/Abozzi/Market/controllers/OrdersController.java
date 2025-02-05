package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import mirkoabozzi.Abozzi.Market.dto.OrdersDTO;
import mirkoabozzi.Abozzi.Market.dto.OrdersStateDTO;
import mirkoabozzi.Abozzi.Market.entities.Order;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.OrdersService;
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

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Order saveOrder(@RequestBody @Validated OrdersDTO payload, BindingResult validation) throws MessagingException {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.ordersService.saveOrder(payload);
        }
    }

    //GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Order> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "orderDate") String sortBy) {
        return this.ordersService.getAllOrders(page, size, sortBy);
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
    public Page<Order> getMyOrders(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "orderDate") String sortBy,
                                   @AuthenticationPrincipal User userAuthenticated
    ) {
        return this.ordersService.getMyOrders(page, size, sortBy, userAuthenticated.getId());
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
    public Order getMyOrderById(@PathVariable UUID id, @AuthenticationPrincipal User userAuthenticated) {
        return this.ordersService.findMyOrderById(id, userAuthenticated.getId());
    }

    //UPDATE ORDER STATE
    @PutMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order updateOrder(@RequestBody @Validated OrdersStateDTO payload, BindingResult validation) throws MessagingException {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.ordersService.updateOrderState(payload);
        }
    }

    // GET BY USER EMAIL
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Order> getOrderByUserEmail(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "orderDate") String sortBy,
                                           @RequestParam String email) {
        return this.ordersService.findByUserEmail(page, size, sortBy, email);
    }

    //GET ORDER BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order getOrderById(@PathVariable UUID id) {
        return this.ordersService.findById(id);
    }
}