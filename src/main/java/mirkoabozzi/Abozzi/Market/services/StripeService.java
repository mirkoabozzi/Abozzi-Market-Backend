package mirkoabozzi.Abozzi.Market.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import mirkoabozzi.Abozzi.Market.dto.StripeDTO;
import mirkoabozzi.Abozzi.Market.entities.Stripe;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.StripeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class StripeService {
    @Autowired
    private StripeRepository stripeRepository;

    public String createPaymentSession(StripeDTO payload) throws StripeException {
        SessionCreateParams.Builder sessionCreateParamsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(payload.approvedUrl() + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(payload.failedUrl());
        sessionCreateParamsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("EUR")
                                        .setUnitAmountDecimal(BigDecimal.valueOf(payload.sum() * 100))
                                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Abozzi Market")
                                                .build())
                                        .build())
                        .build());
        Session session = Session.create(sessionCreateParamsBuilder.build());
        return session.getUrl();
    }

    public Stripe findBySessionId(String sessionId) {
        return this.stripeRepository.findBySessionId(sessionId).orElseThrow(() -> new NotFoundException("Stripe payment whit ID " + sessionId + " not found"));
    }

    public String verifyStripePayment(String sessionId) throws StripeException {

        if (this.stripeRepository.existsBySessionId(sessionId))
            throw new BadRequestException("Payment with id " + sessionId + " already on DB");

        Session session = Session.retrieve(sessionId);
        if (session.getStatus().equals("complete")) {
            Stripe payment = new Stripe();
            payment.setSessionId(session.getId());
            payment.setPaymentIntentId(session.getPaymentIntent());
            payment.setTotal(session.getAmountTotal() / 100.0);
            payment.setCurrency(session.getCurrency());
            payment.setStatus("COMPLETED");
            payment.setPaymentDate(LocalDateTime.now());
            this.stripeRepository.save(payment);
        }
        return session.getStatus();
    }

}
