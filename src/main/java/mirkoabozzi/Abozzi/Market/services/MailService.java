package mirkoabozzi.Abozzi.Market.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mirkoabozzi.Abozzi.Market.dto.MailDTO;
import mirkoabozzi.Abozzi.Market.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Value("${gmail.mail.from}")
    String from;
    @Value("${cors.config.local.host.router}")
    private String localHostRouter;
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

    public void resetPasswordRequest(User user) throws MessagingException {
        MimeMessage msg = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Richiesta reset password");
        String content =
                "<!DOCTYPE html>" +
                        "<html lang='it'>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "<title>Cambio Password</title>" +
                        "</head>" +
                        "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; text-align: center;'>" +
                        "<div style='background-color: white; padding: 40px; max-width: 500px; margin: auto; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>" +
                        "<h1 style='color: #1a51bf;'>Password Reset</h1>" +
                        "<p style='color: #333;'>Hai richiesto il reset della password, clicca sul pulsante qui sotto per cambiare la tua password!</p>" +
                        "<a href='" + localHostRouter + "/passwordReset/userId=" + user.getId() + "' " +
                        "style='background-color: #1a51bf; color: white; padding: 15px 25px; border: none; border-radius: 5px; font-size: 16px; text-decoration: none; display: inline-block; margin-top: 20px;'>Cambia Password</a>" +
                        "<p style='color: #333;'>Se non sei stato tu a richiedere il cambio password, ignora questa email!</p>" +
                        "<p style='color: #333;'>Abozzi Market SNC</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>";
        helper.setText(content, true);
        this.javaMailSender.send(msg);
    }
}
