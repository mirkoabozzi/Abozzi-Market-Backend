package mirkoabozzi.Abozzi.Market.controllers;

import com.stripe.exception.StripeException;
import mirkoabozzi.Abozzi.Market.dto.StripeDTO;
import mirkoabozzi.Abozzi.Market.entities.Stripe;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.repositories.StripeRepository;
import mirkoabozzi.Abozzi.Market.services.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stripe")
public class StripeController {
    @Autowired
    private StripeService stripeService;
    @Autowired
    private StripeRepository stripeRepository;

    //POST CREATE STRIPE SESSION
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public String createSession(@RequestBody @Validated StripeDTO payload, BindingResult validation) throws StripeException {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.stripeService.createPaymentSession(payload);
        }
    }

    // GET VERIFY AND SAVE PAYMENT
    @GetMapping("/verify")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public String verifyStripePayment(@RequestParam String sessionId) throws StripeException {
        return this.stripeService.verifyStripePayment(sessionId);
    }

    //REPORT BY DATE
    @GetMapping("/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Stripe> findByDate(@RequestParam LocalDateTime startDate,
                                   @RequestParam LocalDateTime endDate) {
        return this.stripeService.findByPaymentDate(startDate, endDate);
    }
}
