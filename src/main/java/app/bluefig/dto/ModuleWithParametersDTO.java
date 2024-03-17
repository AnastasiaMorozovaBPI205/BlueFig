package app.bluefig.dto;

import app.bluefig.model.Parameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModuleWithParametersDTO {
    private String id;
    private String name;
    List<Parameter> parameterList;
}
