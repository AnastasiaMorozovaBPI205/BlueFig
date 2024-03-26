package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class NotificationJpa {
    @Id
    private String id;
    private String text;
    private LocalDateTime datetime;
    @Column(name = "user_id")
    private String userId;
}
