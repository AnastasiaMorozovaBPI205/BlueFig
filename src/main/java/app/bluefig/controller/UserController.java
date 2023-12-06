package app.bluefig.controller;

import app.bluefig.MapStructMapper;
import app.bluefig.entity.UserJpa;
import app.bluefig.model.User;
import app.bluefig.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public void addNewUser(String name, String email, String password, String role, String permissionCode,
                           LocalDate birthday, String sex, String doctorId) {
        userService.addUserList(name, email, password, role, permissionCode, birthday, sex, doctorId);
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

    @GetMapping("/patients_list/{doctor_id} ")
    public List<User> getDoctorsPatients(@PathVariable("doctor_id") String doctorId) {
        List<UserJpa> userJpas = userService.findDoctorsPatientsJpa(doctorId);
        return mapper.UserJpasToUsers(userJpas);
    }
}
