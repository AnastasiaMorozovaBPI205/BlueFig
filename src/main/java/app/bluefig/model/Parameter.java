package app.bluefig.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Parameter {
    private String id;
    private String dataType;
    private String name;
    private String description;
}
