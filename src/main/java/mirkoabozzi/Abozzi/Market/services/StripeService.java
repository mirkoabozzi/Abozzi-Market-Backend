package mirkoabozzi.Abozzi.Market.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import mirkoabozzi.Abozzi.Market.dto.request.StripeDTO;
import mirkoabozzi.Abozzi.Market.entities.Stripe;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.StripeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StripeService {
    @Autowired
    private StripeRepository stripeRepository;

    //PAYMENT SESSION
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

    //FIND BY SESSION ID
    public Stripe findBySessionId(String sessionId) {
        return this.stripeRepository.findBySessionId(sessionId).orElseThrow(() -> new NotFoundException("Stripe payment whit ID " + sessionId + " not found"));
    }

    //VERIFY AND SEVE STRIPE PAYMENT
    public String verifyStripePayment(String sessionId) throws StripeException {

        if (this.stripeRepository.existsBySessionId(sessionId))
            throw new BadRequestException("Payment with id " + sessionId + " already on DB");

        Session session = Session.retrieve(sessionId);
        if (session.getStatus().equals("complete")) {

            Stripe payment = new Stripe(
                    LocalDateTime.now(),
                    session.getAmountTotal() / 100.0,
                    "COMPLETED",
                    "Stripe",
                    session.getId(),
                    session.getPaymentIntent(),
                    session.getCurrency());

            this.stripeRepository.save(payment);
        }
        return session.getStatus();
    }

    //FIND PAYMENT BY DATE
    public List<Stripe> findByPaymentDate(LocalDateTime startDate, LocalDateTime endDate) {
        return this.stripeRepository.findByPaymentDateBetween(startDate, endDate);
    }
}
