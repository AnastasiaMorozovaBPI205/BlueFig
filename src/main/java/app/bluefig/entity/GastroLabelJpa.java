package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "labels", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class GastroLabelJpa {
    @Id
    private String id;
    private String name;
    @Column(name = "is_red_flag")
    private String isRedFlag;
}
