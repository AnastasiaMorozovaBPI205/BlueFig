package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctor_parameters", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class DoctorParameterJpa {
    @Id
    private String id;
    private String name;
    @Column(name = "module_id")
    private String moduleId;
    @Column(name = "data_type")
    private String dataType;
}
