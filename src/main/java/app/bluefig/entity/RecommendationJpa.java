package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "doctor_recommendation", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class RecommendationJpa {
    @Id
    private UUID id;
    @Column(name = "doctor_id")
    private UUID doctorId;
    @Column(name = "patient_id")
    private UUID patientId;
    private LocalDateTime datetime;
    private String recommendation;

    @Column(name = "doctor_name")
    private String doctorName;
}
