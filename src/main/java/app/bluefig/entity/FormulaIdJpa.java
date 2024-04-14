package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class FormulaIdJpa {
    String name;
    @Column(name = "feature_name")
    String featureName;
    String value;
    String units;
}
