package mirkoabozzi.Abozzi.Market.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import mirkoabozzi.Abozzi.Market.dto.request.PayPalDTO;
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
public class PayPalService {
    @Autowired
    private APIContext apiContext;
    @Autowired
    private PayPalRepository payPalRepository;

    // CREATE PAYPAL PAYMENT
    public Payment createPayment(PayPalDTO payload) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(payload.currency());
        amount.setTotal(String.format(Locale.forLanguageTag(payload.currency()), "%.2f", payload.sum()));

        Transaction transaction = new Transaction();
        transaction.setDescription(payload.description());
        transaction.setAmount(amount);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(payload.method());

        Payment payment = new Payment();
        payment.setIntent(payload.intent());
        payment.setPayer(payer);
        payment.setTransactions(transactionList);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(payload.cancelUrl());
        redirectUrls.setReturnUrl(payload.successUrl());
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    //EXECUTE PAYPAL PAYMENT
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        PayPal newPayment = new PayPal(
                LocalDateTime.now(),
                Double.valueOf(executedPayment.getTransactions().getFirst().getAmount().getTotal()),
                executedPayment.getState(),
                executedPayment.getTransactions().getFirst().getDescription(),
                payerId,
                executedPayment.getId());

        this.payPalRepository.save(newPayment);

        return executedPayment;
    }

    //FIND BY ID
    public PayPal findByPaymentId(String id) {
        return this.payPalRepository.findByPaymentId(id).orElseThrow(() -> new NotFoundException("Pay Pal payment whit ID " + id + " not found"));
    }

    //FIND BY DATE
    public List<PayPal> findByPaymentDate(LocalDateTime startDate, LocalDateTime endDate) {
        return this.payPalRepository.findByPaymentDateBetween(startDate, endDate);
    }
}
