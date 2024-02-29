package app.bluefig.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Recommendation {
    private String id;
    private String doctorId;
    private String patientId;
    private LocalDateTime datetime;
    private String recommendation;
}
