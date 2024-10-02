package mirkoabozzi.Abozzi.Market.tools;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import mirkoabozzi.Abozzi.Market.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailgunSender {
    private String apiKey;
    private String domainName;
    private String mailgunSender;

    public MailgunSender(@Value("${mailgun.key}") String apiKey, @Value("${mailgun.domain}") String domainName, @Value("${mailgun.sender}") String mailgunSender) {
        this.apiKey = apiKey;
        this.domainName = domainName;
        this.mailgunSender = mailgunSender;

    }

    public void sendRegistrationEmail(User recipient) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.mailgunSender)
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Thanks " + recipient.getName() + " " + recipient.getSurname() + " to joining us!")
                .queryString("template", "registration")
                .asJson();
        System.out.println(response.getBody());
    }

    public void sendOrderCreatedEmail(User recipient) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.mailgunSender)
                .queryString("to", recipient.getEmail())
                .queryString("subject", recipient.getName() + " you order is on de way!")
                .queryString("template", "order")
                .asJson();
        System.out.println(response.getBody());
    }
}
