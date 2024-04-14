package app.bluefig.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

@Entity
@Table(name = "product_group", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class ProductGroupJpa {
    @Id
    String id;
    String name;
}
