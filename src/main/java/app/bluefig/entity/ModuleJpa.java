package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "questionary", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class ModuleJpa {
    @Id
    private UUID id;
    @Column(name = "patient_id")
    private String patientId;
    @Column(name = "doctor_id")
    private String doctorId;
}
