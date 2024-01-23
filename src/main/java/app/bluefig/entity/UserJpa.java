package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String username;
    private String firstname;
    private String lastname;
    private String fathername;
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "role_id")
    private String roleId;
    private LocalDate birthday;
    private String sex;
}
