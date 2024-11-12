package mirkoabozzi.Abozzi.Market.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {
    @Value("${api.stripe.key}")
    private String stripeKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }
}
