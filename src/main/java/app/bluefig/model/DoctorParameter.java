package app.bluefig.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorParameter {
    private String id;
    private String name;
    private String moduleId;
    private String dataType;
}
