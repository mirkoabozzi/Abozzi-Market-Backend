package mirkoabozzi.Abozzi.Market.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import mirkoabozzi.Abozzi.Market.entities.PayPal;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.PayPalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PayPalService {
    private final APIContext apiContext;
    @Autowired
    private PayPalRepository payPalRepository;

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactionList);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        PayPal newPayment = new PayPal();
        newPayment.setPayerId(payerId);
        newPayment.setPaymentDate(LocalDateTime.now());
        newPayment.setTotal(Double.valueOf(executedPayment.getTransactions().getFirst().getAmount().getTotal()));
        newPayment.setDescription(executedPayment.getTransactions().getFirst().getDescription());
        newPayment.setPaymentId(executedPayment.getId());
        newPayment.setStatus(executedPayment.getState());

        this.payPalRepository.save(newPayment);

        return executedPayment;
    }

    public PayPal findById(String id) {
        return this.payPalRepository.findByPaymentId(id).orElseThrow(() -> new NotFoundException("Payment whit ID " + id + " not found"));
    }
}
