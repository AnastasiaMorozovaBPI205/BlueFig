package app.bluefig.controller;

import app.bluefig.MapStructMapper;
import app.bluefig.entity.UserJpa;
import app.bluefig.model.User;
import app.bluefig.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

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
        String passwordHash = data.get("passwordHash");
        String roleId = data.get("roleId");
        LocalDate birthday = LocalDate.parse(data.get("birthday"));
        String sex = data.get("sex");

        userService.addUserList(username, firstname, lastname, email, passwordHash, roleId, birthday, sex);
        System.out.println("user added successfully");
    }

    /**
     * Поиск данных пользователя по его id.
     * @param id пользователя
     * @return данные пользователя
     */
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        UserJpa userJpa = userService.findUserJpa(id);
        return mapper.UserJpaToUser(userJpa);
    }

    /**
     * Удаление пользователя из БД по id.
     * @param id пользователя
     */
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable String id) {
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
}
