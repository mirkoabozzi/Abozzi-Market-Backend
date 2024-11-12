package mirkoabozzi.Abozzi.Market.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import mirkoabozzi.Abozzi.Market.dto.StripeDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {

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
}
