package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health")
public class HealthController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String healthCheck() {
        return "UP";
    }
}
