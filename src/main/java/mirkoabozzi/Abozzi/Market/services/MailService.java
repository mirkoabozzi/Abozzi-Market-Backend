package mirkoabozzi.Abozzi.Market.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mirkoabozzi.Abozzi.Market.dto.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Value("${gmail.mail.from}")
    String from;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(MailDTO payload) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(from);
        helper.setSubject("Nuovo messaggio da: " + payload.name());
        String content = "<h1>Nuovo messaggio in Abozzi Market</h1>" +
                "<p><strong>Nome:</strong> " + payload.name() + "</p>" +
                "<p><strong>Email:</strong> " + payload.email() + "</p>" +
                "<p><strong>Messaggio:</strong></p>" +
                "<p>" + payload.text() + "</p>";
        helper.setText(content, true);
        javaMailSender.send(msg);
    }
}
