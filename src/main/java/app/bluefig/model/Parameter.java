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

    public Parameter(Parameter other) {
        this.id = other.id;
        this.dataType = other.dataType;
        this.name = other.name;
        this.description = other.description;
        this.moduleId = other.moduleId;
        this.value = other.value;
    }
}
