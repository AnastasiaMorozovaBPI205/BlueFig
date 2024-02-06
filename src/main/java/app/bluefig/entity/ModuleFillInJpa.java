package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "questionary_fillin", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class ModuleFillInJpa {
    @Id
    private UUID id;
    @Column(name = "questionary_id")
    private String questionaryId;
    private LocalDateTime datetime;
}
