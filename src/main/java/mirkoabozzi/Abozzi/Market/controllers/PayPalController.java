package mirkoabozzi.Abozzi.Market.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.NoArgsConstructor;
import mirkoabozzi.Abozzi.Market.services.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NoArgsConstructor
@RequestMapping("/pay")
public class PayPalController {
    @Autowired
    PayPalService payPalService;

    //CREATE PAYMENT POST
    @PostMapping
    public String createPayment(@RequestParam("sum") Double sum,
                                @RequestParam("currency") String currency,
                                @RequestParam("method") String method,
                                @RequestParam("intent") String intent,
                                @RequestParam("description") String description,
                                @RequestParam("cancelUrl") String cancelUrl,
                                @RequestParam("successUrl") String successUrl
    ) throws PayPalRESTException {
        Payment payment = this.payPalService.createPayment(sum, currency, method, intent, description, cancelUrl, successUrl);
        for (Links links : payment.getLinks()) {
            if (links.getRel().equals("approval_url")) {
                return "redirect:" + links.getHref();
            }
        }
        return "Error";
    }

    //EXECUTE PAYMENT POST
    @PostMapping("/execute")
    public String executePayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerId") String payerId,
            @RequestParam("approvedUrl") String approvedUrl,
            @RequestParam("failedUrl") String failedUrl
    ) throws PayPalRESTException {
        Payment payment = this.payPalService.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {
            return "redirect:/" + approvedUrl;
        }
        return "redirect:/" + failedUrl;
    }
}
