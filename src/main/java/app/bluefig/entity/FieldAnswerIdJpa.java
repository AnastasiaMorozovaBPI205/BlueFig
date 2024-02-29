package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class FieldAnswerIdJpa {
    @Column(name = "fillin_id")
    String fillIn;
    @Column(name = "field_id")
    String fieldId;
}
