package app.bluefig.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String permissionCode;
    private LocalDate birthday;
    private String sex;
    private String doctorId;
}
