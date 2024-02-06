package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "questionary_field", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class ModuleFieldJpa {
    @Id
    private UUID id;
    @Column(name = "questionary_id")
    private String questionaryId;
    @Column(name = "order_number")
    private Integer orderNumber;
    private Integer frequency;
    @Column(name = "parameter_id")
    private String parameterId;
}
