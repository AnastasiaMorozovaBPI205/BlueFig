package app.bluefig.service;

import app.bluefig.entity.UserJpa;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    void addUserList(String name, String email, String password, String role, String permissionCode,
                     LocalDate birthday, String sex, String doctorId);

    UserJpa findUserJpa(String id);

    void deleteUserJpa(String id);

    List<UserJpa>  findDoctorsPatientsJpa(String doctorId);
}
