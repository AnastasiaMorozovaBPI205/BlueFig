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
    private UUID id;
    private UUID doctorId;
    private UUID patientId;
    private LocalDateTime datetime;
    private String recommendation;
    private String doctorName;
}
