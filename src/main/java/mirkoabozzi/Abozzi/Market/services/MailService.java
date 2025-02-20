package mirkoabozzi.Abozzi.Market.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mirkoabozzi.Abozzi.Market.dto.request.MailDTO;
import mirkoabozzi.Abozzi.Market.entities.Order;
import mirkoabozzi.Abozzi.Market.entities.OrderDetail;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.enums.RegistrationMethod;
import mirkoabozzi.Abozzi.Market.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MailService {
    @Value("${gmail.mail.from}")
    String from;
    @Value("${cors.config.front.end.url}")
    private String frontEndUrl;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UsersRepository usersRepository;

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
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setTokenDuration(LocalDateTime.now().plusMinutes(30));
        this.usersRepository.save(user);

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
                        "<p style='color: #333;'>Il link sarà valido per 30 minuti!</p>" +
                        "<a href='" + frontEndUrl + "/passwordReset/token=" + user.getResetPasswordToken() + "' " +
                        "style='background-color: #1a51bf; color: white; padding: 15px 25px; border: none; border-radius: 5px; font-size: 16px; text-decoration: none; display: inline-block; margin-top: 20px;'>Cambia Password</a>" +
                        "<p style='color: #333;'>Se non sei stato tu a richiedere il cambio password, ignora questa email!</p>" +
                        "<p style='color: #333;'>Abozzi Market SNC</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>";
        helper.setText(content, true);
        this.javaMailSender.send(msg);
    }

    public void confirmPasswordChanged(User user) throws MessagingException {
        MimeMessage msg = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Cambio password confermato");

        String content =
                "<!DOCTYPE html>" +
                        "<html lang='it'>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "<title>Password cambiata con successo</title>" +
                        "</head>" +
                        "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; text-align: center;'>" +
                        "<div style='background-color: white; padding: 40px; max-width: 500px; margin: auto; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>" +
                        "<h1 style='color: #1a51bf;'>Cambio Password</h1>" +
                        "<p style='color: #333;'>La tua password è stata modificata con successo, da questo momento potrai utilizzare la tua nuova password per accedere.</p>" +
                        "<a href='" + frontEndUrl + "' " +
                        "style='background-color: #1a51bf; color: white; padding: 15px 25px; border: none; border-radius: 5px; font-size: 16px; text-decoration: none; display: inline-block; margin-top: 20px;'>Continua i tuoi acquisti</a>" +
                        "<p style='color: #333;'>Abozzi Market SNC</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(content, true);
        this.javaMailSender.send(msg);
    }

    public void orderConfirmationEmail(User user, Order order, List<OrderDetail> orderDetails) throws MessagingException {
        MimeMessage msg = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Riepilogo ordine");

        String shippingInfo = (order.getShipment() != null)
                ? order.getShipment().getCity() + " " + order.getShipment().getAddress() + " " + order.getShipment().getNumber()
                : "Ritiro in negozio";

        String content =
                "<!DOCTYPE html>" +
                        "<html lang='it'>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "<title>Conferma Ordine</title>" +
                        "</head>" +
                        "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; text-align: center;'>" +
                        "<div style='background-color: white; padding: 40px; max-width: 500px; margin: auto; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>" +
                        "<h1 style='color: #1a51bf;'>Conferma Ordine</h1>" +
                        "<p style='color: #333;'>Ciao " + user.getName() + " " + user.getSurname() + ",</p>" +
                        "<p style='color: #333;'>Grazie per aver effettuato un ordine su Abozzi Market SNC!</p>" +
                        "<p style='color: #333;'>Ecco i dettagli del tuo ordine: " + order.getId() + " </p>" +
                        "<table style='width: 100%; margin: 20px 0; text-align: left;'>" +
                        "<tr>" +
                        "<th>Data Ordine:</th>" +
                        "<td>" + order.getOrderDate().toLocalDate() + "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<th>Stato Ordine:</th>" +
                        "<td>" + order.getOrdersState() + "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<th>Metodo di Pagamento:</th>" +
                        "<td>" + order.getPayment().getClass().getSimpleName() + " - Totale: " + order.getPayment().getTotal() + "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<th>Indirizzo di Spedizione:</th>" +
                        "<td>" + shippingInfo + "</td>" +
                        "</tr>" +
                        "</table>" +
                        "<h2>Dettagli Prodotti:</h2>" +
                        "<table style='width: 100%; margin: 20px 0; text-align: left; border-collapse: collapse;'>" +
                        "<thead>" +
                        "<tr>" +
                        "<th style='border-bottom: 1px solid #ddd; padding: 8px;'>Prodotto</th>" +
                        "<th style='border-bottom: 1px solid #ddd; padding: 8px;'>Quantità</th>" +
                        "<th style='border-bottom: 1px solid #ddd; padding: 8px;'>Prezzo</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>";

        for (OrderDetail detail : orderDetails) {
            Product product = detail.getProduct();
            content += "<tr>" +
                    "<td style='border-bottom: 1px solid #ddd; padding: 8px;'>" + product.getName() + "</td>" +
                    "<td style='border-bottom: 1px solid #ddd; padding: 8px;'>" + detail.getQuantity() + "</td>" +
                    "<td style='border-bottom: 1px solid #ddd; padding: 8px;'>" + detail.getPrice() + "</td>" +
                    "</tr>";
        }
        content += "</tbody>" +
                "</table>" +
                "<p style='color: #333;'>Ti invieremo una notifica appena il tuo ordine verrà spedito.</p>" +
                "<p style='color: #333;'>Grazie ancora per aver scelto Abozzi Market SNC!</p>" +
                "<small style='color: #333;'>Abozzi Market SNC</small>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setText(content, true);
        this.javaMailSender.send(msg);
    }

    public void userRegistrationEmail(User user) throws MessagingException {
        MimeMessage msg = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Benvenuto su Abozzi Market!");

        String content =
                "<!DOCTYPE html>" +
                        "<html lang='it'>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "<title>Benvenuto su Abozzi Market!</title>" +
                        "</head>" +
                        "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; text-align: center;'>" +
                        "<div style='background-color: white; padding: 40px; max-width: 500px; margin: auto; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>" +
                        "<h1 style='color: #1a51bf;'>Benvenuto, " + user.getName() + " " + user.getSurname() + "!</h1>" +
                        "<p style='color: #333;'>Grazie per esserti unito a Abozzi Market!</p>" +
                        "<p style='color: #333;'>Siamo lieti di averti con noi.</p>" +
                        "<p style='color: #333;'>Puoi ora esplorare i nostri prodotti e iniziare a fare acquisti.</p>" +
                        (user.getRegistrationMethod() == RegistrationMethod.FORM ? "<a href='" + frontEndUrl + "/verify/token=" + user.getVerificationToken() + "' " +
                                "style='background-color: #1a51bf; color: white; padding: 15px 25px; border: none; border-radius: 5px; font-size: 16px; text-decoration: none; display: inline-block; margin-top: 20px;'>Conferma email</a>" : "") +
                        "<p style='color: #333; margin-top: 20px;'>Se hai domande o hai bisogno di assistenza, non esitare a contattarci.</p>" +
                        "<small style='color: #333;'>Abozzi Market SNC</small>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(content, true);
        this.javaMailSender.send(msg);
    }

    public void orderStatusEmail(Order order) throws MessagingException {
        MimeMessage msg = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        User user = order.getUser();
        helper.setTo(user.getEmail());
        helper.setSubject("Aggiornamento Stato Ordine #" + order.getId());

        String orderStatus;
        switch (order.getOrdersState()) {
            case PROCESSING -> orderStatus = "In lavorazione";
            case CANCELLED -> orderStatus = "Cancellato";
            case SHIPPED -> orderStatus = "Spedito";
            case IN_TRANSIT -> orderStatus = "In transito";
            case ON_DELIVERY -> orderStatus = "In consegna";
            case DELIVERED -> orderStatus = "Consegnato";
            case READY_TO_PICKUP -> orderStatus = "Pronto per il ritiro";
            default -> orderStatus = String.valueOf(order.getOrdersState());
        }

        String content =
                "<!DOCTYPE html>" +
                        "<html lang='it'>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "<title>Aggiornamento Stato Ordine</title>" +
                        "</head>" +
                        "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; text-align: center;'>" +
                        "<div style='background-color: white; padding: 40px; max-width: 500px; margin: auto; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>" +
                        "<h1 style='color: #1a51bf;'>Aggiornamento del tuo ordine: " + order.getId() + "</h1>" +
                        "<p style='color: #333;'>Ciao " + user.getName() + " " + user.getSurname() + ",</p>" +
                        "<p style='color: #333;'>Ti informiamo che lo stato del tuo ordine è cambiato.</p>" +
                        "<p style='color: #333;'>Nuovo stato: <strong>" + orderStatus + "</strong></p>" +
                        "<p style='color: #333;'>Continua a monitorare lo stato del tuo ordine direttamente sul tuo account.</p>" +
                        "<p style='color: #333;'>Grazie per aver scelto Abozzi Market!</p>" +
                        "<a href='" + frontEndUrl + "/profile/orders/details/" + order.getId() + "' " +
                        "style='background-color: #1a51bf; color: white; padding: 15px 25px; border: none; border-radius: 5px; font-size: 16px; text-decoration: none; display: inline-block; margin-top: 20px;'>Visualizza il tuo ordine</a>" +
                        "<p style='color: #333; margin-top: 20px;'>Se hai domande, non esitare a contattarci.</p>" +
                        "<small style='color: #333;'>Abozzi Market SNC</small>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(content, true);
        this.javaMailSender.send(msg);
    }
}
