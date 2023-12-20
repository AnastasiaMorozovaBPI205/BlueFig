package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "user", schema = "sma_service")
@Getter
@Setter
@NoArgsConstructor
public class UserJpa {
    @Id
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;
    @Column(name = "permission_code")
    private String permissionCode;
    private LocalDate birthday;
    private String sex;
    @Column(name = "doctor_id")
    private String doctorId;
}
