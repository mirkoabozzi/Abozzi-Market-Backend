package mirkoabozzi.Abozzi.Market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayPalRespDTO {
    private UUID id;
    private LocalDateTime paymentDate;
    private Double total;
    private String status;
    private String description;
    private String payerId;
    private String paymentId;
}
