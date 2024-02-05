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
@Table(name = "parameters", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class ParameterJpa {
    @Id
    private UUID id;
    @Column(name = "data_type")
    private String dataType;
    private String name;
    private String description;
}
