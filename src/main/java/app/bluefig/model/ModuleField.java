package app.bluefig.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ModuleField {
    private String id;
    private String questionaryId;
    private Integer orderNumber;
    private Integer frequency;
    private String parameterId;
}
