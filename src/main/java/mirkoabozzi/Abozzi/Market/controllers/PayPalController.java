package mirkoabozzi.Abozzi.Market.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.tags.Tag;
import mirkoabozzi.Abozzi.Market.dto.request.PayPalDTO;
import mirkoabozzi.Abozzi.Market.dto.request.PayPalExecuteDTO;
import mirkoabozzi.Abozzi.Market.dto.response.PayPalRespDTO;
import mirkoabozzi.Abozzi.Market.entities.PayPal;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.PayPalService;
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
@RequestMapping("/pay")
@Tag(name = "PayPal")
public class PayPalController {
    @Autowired
    private PayPalService payPalService;
    @Autowired
    private ModelMapper modelMapper;

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

    //REPORT BY DATE
    @GetMapping("/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PayPalRespDTO> findByDate(@RequestParam LocalDateTime startDate,
                                          @RequestParam LocalDateTime endDate
    ) {
        List<PayPal> payPalList = this.payPalService.findByPaymentDate(startDate, endDate);
        return payPalList.stream().map(payPal -> modelMapper.map(payPal, PayPalRespDTO.class)).toList();
    }
}
