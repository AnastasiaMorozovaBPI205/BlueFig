package app.bluefig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user", schema = "main")
@Getter
@Setter
@NoArgsConstructor
public class UserJpa {
    @Id
    private String id;
    private String name;
    private String password;
    private String role;
    @Column(name = "permission_code")
    private String permissionCode;
    private LocalDate birthday;
    private String sex;
    @Column(name = "doctor_id")
    private String doctorId;
}
