package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "patient_hierarchy", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class PatientHierarchyJpa {
    @Id
    @Column(name = "patient_id")
    String patientId;
    int number;
}
