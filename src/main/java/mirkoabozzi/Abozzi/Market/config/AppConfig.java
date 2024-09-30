package mirkoabozzi.Abozzi.Market.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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
}
