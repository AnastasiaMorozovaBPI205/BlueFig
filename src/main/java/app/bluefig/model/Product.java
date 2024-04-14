package app.bluefig.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Product {
    String id;
    String name;
    String groupId;
    String protein;
    String lipids;
    String energy;
    String carbohydrates;
}
