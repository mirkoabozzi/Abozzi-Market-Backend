package mirkoabozzi.Abozzi.Market.controllers;

import com.stripe.exception.StripeException;
import mirkoabozzi.Abozzi.Market.dto.StripeDTO;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.repositories.StripeRepository;
import mirkoabozzi.Abozzi.Market.services.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/stripe")
public class StripeController {
    @Autowired
    private StripeService stripeService;
    @Autowired
    private StripeRepository stripeRepository;

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
}
