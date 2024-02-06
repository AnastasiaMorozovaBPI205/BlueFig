package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "questionary_answer", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class FieldAnswerJpa {
    String value;
    @Column(name = "fillin_id")
    String fillIn;
    @Column(name = "field_id")
    String fieldId;
}
