package app.bluefig.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;
    private String role;
    private String permissionCode;
    private LocalDate birthday;
    private String sex;
    private String doctorId;
}
