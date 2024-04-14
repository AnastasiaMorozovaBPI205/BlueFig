package app.bluefig.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nutrition_feature_value", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class FormulaJpa {
    @EmbeddedId
    FormulaIdJpa id;
}
