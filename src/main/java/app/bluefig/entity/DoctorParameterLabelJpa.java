package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctor_parameters_labels", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class DoctorParameterLabelJpa {
    @Id
    String id;
    String name;
    @Column(name = "parameter_id")
    String parameterId;
}
