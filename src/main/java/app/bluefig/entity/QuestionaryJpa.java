package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "questionary", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class QuestionaryJpa {
    @Id
    private String id;
    @Column(name = "patient_id")
    private String patientId;
    @Column(name = "doctor_id")
    private String doctorId;
    @Column(name = "module_id")
    private String moduleId;
    private int frequency;
    private LocalDateTime datetime;
}
