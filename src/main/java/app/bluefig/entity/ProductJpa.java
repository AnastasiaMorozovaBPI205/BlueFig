package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class ProductJpa {
    @Id
    String id;
    String name;
    @Column(name = "group_id")
    String groupId;
    String protein;
    String lipids;
    String energy;
    String carbohydrates;
}
