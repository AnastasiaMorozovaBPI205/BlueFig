package app.bluefig.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorParameterFillIn {
    String questionaryId;
    String parameterId;
    String value;
}
