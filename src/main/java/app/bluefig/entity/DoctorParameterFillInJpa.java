package app.bluefig.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "questionary_doctor_parameters", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class DoctorParameterFillInJpa {
    @EmbeddedId
    DoctorParameterFillInIdJpa id;
    String value;
}
