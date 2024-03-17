package app.bluefig.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modules", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class ModuleJpa {
    @Id
    private String id;
    private String name;
}
