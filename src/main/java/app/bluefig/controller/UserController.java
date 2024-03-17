package app.bluefig.controller;

import app.bluefig.MapStructMapper;
import app.bluefig.entity.UserJpa;
import app.bluefig.model.User;
import app.bluefig.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private MapStructMapper mapper;

    /**
     * Добавление нового пользователя в базу данных при регистрации.
     */
    @PostMapping("/user")
    public void addNewUser(@RequestBody HashMap<String, String> data) {
        String username = data.get("username");
        String firstname = data.get("firstname");
        String lastname = data.get("lastname");
        String email = data.get("email");
        LocalDate birthday = LocalDate.parse(data.get("birthday").substring(0, 10));
        String sex = data.get("sex");
        String fathername = data.get("fathername");
        String roleId = data.get("roleId");

        String password = data.get("passwordHash");
        String passwordHash = String.valueOf(password.hashCode());

        userService.addUserList(username, firstname, lastname, fathername, email, passwordHash, roleId, birthday, sex);
        System.out.println("user added successfully");
    }

    /**
     * Авторизация пользователя по логину и паролю.
     * @param data данные пользователя
     */
    @PostMapping("/login")
    public User authorization(@RequestBody HashMap<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String passwordHash = String.valueOf(password.hashCode());

        UserJpa userJpa = userService.findUserJpaByUsernamePasswordHash(username, passwordHash);

        if (userJpa == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Ошибка авторизации!"
            );
        }

        return mapper.UserJpaToUser(userJpa);
    }

    /**
     * Поиск данных пользователя по его id.
     * @param id пользователя
     * @return данные пользователя
     */
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable String id) {
        UserJpa userJpa = userService.findUserJpaById(id);
        return mapper.UserJpaToUser(userJpa);
    }

    /**
     * Удаление пользователя из БД по id.
     * @param id пользователя
     */
    @DeleteMapping("/user/{id}")
    public void deleteUserById(@PathVariable String id) {
        userService.deleteUserJpa(id);
    }

    /**
     * Поиск пациентов врача.
     * @param doctorId врача
     * @return пациентов врача
     */
    @GetMapping("/patients_list/{doctorId}")
    public List<User> getDoctorsPatients(@PathVariable String doctorId) {
        List<UserJpa> userJpas = userService.findDoctorsPatientsJpa(doctorId);
        return mapper.UserJpasToUsers(userJpas);
    }

    /**
     * Поиск врачей пациента.
     * @param patientId пациента
     * @return врачей пациента
     */
    @GetMapping("/doctors_list/{patientId}")
    public List<User> getPatientsDoctors(@PathVariable String patientId) {
        List<UserJpa> userJpas = userService.findPatientsDoctorsJpa(patientId);
        return mapper.UserJpasToUsers(userJpas);
    }

    /**
     * Привязка пациента к врачу.
     * @param patientId пациента
     * @param doctorId врача
     */
    @PostMapping("/link_patient/{patientId}/{doctorId}")
    public void linkPatientToDoctor(@PathVariable String patientId, @PathVariable String doctorId) {
        userService.linkPatientToDoctor(patientId, doctorId);
    }
}
