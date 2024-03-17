package app.bluefig.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Questionary {
    private String id;
    private String patientId;
    private String doctorId;
    private String moduleId;
    private int frequency;
}
