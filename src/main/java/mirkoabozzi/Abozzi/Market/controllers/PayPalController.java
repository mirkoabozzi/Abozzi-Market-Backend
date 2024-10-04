package mirkoabozzi.Abozzi.Market.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.NoArgsConstructor;
import mirkoabozzi.Abozzi.Market.dto.PayPalDTO;
import mirkoabozzi.Abozzi.Market.dto.PayPalExecuteDTO;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@NoArgsConstructor
@RequestMapping("/pay")
public class PayPalController {
    @Autowired
    PayPalService payPalService;

    //CREATE PAYMENT POST
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public String createPayment(@RequestBody @Validated PayPalDTO payload, BindingResult validation) throws PayPalRESTException {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Payment payment = this.payPalService.createPayment(payload);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
            return "Error";
        }
    }

    //EXECUTE PAYMENT POST
    @PostMapping("/execute")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public String executePayment(@RequestBody @Validated PayPalExecuteDTO payload, BindingResult validation) throws PayPalRESTException {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Payment payment = this.payPalService.executePayment(payload.paymentId(), payload.payerId());
            if (payment.getState().equals("approved")) {
                return "redirect:/" + payload.approvedUrl();
            }
            return "redirect:/" + payload.failedUrl();
        }
    }
}
