package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class DoctorParameterFillInIdJpa {
    @Column(name = "questionary_id")
    String questionaryId;
    @Column(name = "parameter_id")
    String parameterId;
}
