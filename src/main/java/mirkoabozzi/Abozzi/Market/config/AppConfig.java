package mirkoabozzi.Abozzi.Market.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class AppConfig {
    @Bean
    public Cloudinary imgUpload(
            @Value("${cloudinary.name}") String name,
            @Value("${cloudinary.key}") String key,
            @Value("${cloudinary.secret}") String secret) {
        Map<String, String> conf = new HashMap<>();
        conf.put("cloud_name", name);
        conf.put("api_key", key);
        conf.put("api_secret", secret);
        return new Cloudinary(conf);
    }

    @Bean
    public JavaMailSenderImpl getJavaMailSender(@Value("${gmail.mail.transport.protocol}") String protocol,
                                                @Value("${gmail.mail.smtp.auth}") String auth,
                                                @Value("${gmail.mail.smtp.starttls.enable}") String starttls,
                                                @Value("${gmail.mail.debug}") String debug,
                                                @Value("${gmail.mail.from}") String from,
                                                @Value("${gmail.mail.from.password}") String password,
                                                @Value("${gmail.smtp.ssl.enable}") String ssl,
                                                @Value("${gmail.smtp.host}") String host,
                                                @Value("${gmail.smtp.port}") String port) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setUsername(from);
        mailSender.setPassword(password);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttls);
        properties.put("mail.debug", debug);
        properties.put("smtp.ssl.enable", ssl);

        return mailSender;
    }
}
