package mirkoabozzi.Abozzi.Market.controllers;

import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import mirkoabozzi.Abozzi.Market.dto.request.StripeDTO;
import mirkoabozzi.Abozzi.Market.dto.response.StripeRespDTO;
import mirkoabozzi.Abozzi.Market.entities.Stripe;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.repositories.StripeRepository;
import mirkoabozzi.Abozzi.Market.services.StripeService;
import org.modelmapper.ModelMapper;
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
@Tag(name = "Stripe")
public class StripeController {
    @Autowired
    private StripeService stripeService;
    @Autowired
    private StripeRepository stripeRepository;
    @Autowired
    private ModelMapper modelMapper;

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
    public List<StripeRespDTO> findByDate(@RequestParam LocalDateTime startDate,
                                          @RequestParam LocalDateTime endDate
    ) {
        List<Stripe> stripeList = this.stripeService.findByPaymentDate(startDate, endDate);
        return stripeList.stream().map(stripe -> this.modelMapper.map(stripe, StripeRespDTO.class)).toList();
    }
}
