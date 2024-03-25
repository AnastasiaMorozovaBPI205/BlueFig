package app.bluefig.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parameter {
    private String id;
    private String dataType;
    private String name;
    private String description;
    private String moduleId;
    private String value;

    public Parameter() {
        id = "";
        dataType = "";
        name = "";
        description = "";
        moduleId = "";
        value = "";
    }
}
