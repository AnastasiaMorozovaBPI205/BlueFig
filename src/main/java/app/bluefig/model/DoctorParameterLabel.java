package app.bluefig.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorParameterLabel {
    String id;
    String name;
    String parameterId;
}
