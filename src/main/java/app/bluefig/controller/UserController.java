package app.bluefig.controller;

import app.bluefig.mapper.MapStructMapper;
import app.bluefig.entity.UserJpa;
import app.bluefig.model.User;
import app.bluefig.service.NotificationServiceImpl;
import app.bluefig.service.QuestionaryServiceImpl;
import app.bluefig.service.RecommendationServiceImpl;
import app.bluefig.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    NotificationServiceImpl notificationService;

    @Autowired
    private QuestionaryServiceImpl questionaryService;

    @Autowired
    private RecommendationServiceImpl recommendationService;

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
        LocalDate birthday = getDate(data.get("birthday"));
        String sex = data.get("sex");
        String fathername = data.get("fathername");
        String roleId = data.get("roleId");
        String password = data.get("password");

        if (username == null || firstname == null || lastname == null || email == null
                || birthday == null || sex == null || fathername == null || roleId == null
                || password == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        String passwordHash = String.valueOf(password.hashCode());

        userService.addUserList(username, firstname, lastname, fathername, email, passwordHash, roleId, birthday, sex);
        System.out.println("user added successfully");
    }

    private LocalDate getDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }

        if (dateStr.length() > 10) {
            return LocalDate.parse(dateStr.substring(0, 10));
        }

        return LocalDate.parse(dateStr);
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
    @RequestMapping(path="/user/{id}", method=RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public User getUserById(@PathVariable String id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        UserJpa userJpa = userService.findUserJpaById(id);
        return mapper.UserJpaToUser(userJpa);
    }

    /**
     * Удаление пользователя из БД по id.
     * @param id пользователя
     */
    @DeleteMapping("/user/{id}")
    public void deleteUserById(@PathVariable String id) {
        final String doctorRoleId = "47b436f0-b4bb-11ee-8c0c-00f5f80cf8ae";
        final String patientRoleId = "49e6f33b-b4bb-11ee-8c0c-00f5f80cf8ae";

        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        UserJpa user = userService.findUserJpaById(id);

        if (user.getRoleId().equals(doctorRoleId)) {
            questionaryService.setDoctorIdUndefined(id);
            recommendationService.setDoctorIdUndefined(id);

            notificationService.deleteNotificationsByUserId(id);
            userService.deleteDoctorFromWatch(id);
        } else if (user.getRoleId().equals(patientRoleId)) {

        }

        userService.deleteUserJpa(id);
    }

    /**
     * Поиск пациентов врача.
     * @param doctorId врача
     * @return пациентов врача
     */
    @RequestMapping(path="/patientsList/{doctorId}", method=RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public List<User> getDoctorsPatients(@PathVariable String doctorId) {
        if (doctorId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<UserJpa> userJpas = userService.findDoctorsPatientsJpa(doctorId);
        return mapper.UserJpasToUsers(userJpas);
    }

    /**
     * Поиск врачей пациента.
     * @param patientId пациента
     * @return врачей пациента
     */
    @RequestMapping(path="/doctorsList/{patientId}", method=RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public List<User> getPatientsDoctors(@PathVariable String patientId) {
        if (patientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<UserJpa> userJpas = userService.findPatientsDoctorsJpa(patientId);
        return mapper.UserJpasToUsers(userJpas);
    }

    /**
     * Привязка пациента к врачу.
     * @param patientId пациента
     * @param doctorId врача
     */
    @PostMapping("/linkPatient/{patientId}/{doctorId}")
    public void linkPatientToDoctor(@PathVariable String patientId, @PathVariable String doctorId) {
        if (patientId == null || doctorId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        userService.linkPatientToDoctor(patientId, doctorId);

        UserJpa doctor = userService.findUserJpaById(doctorId);
        String doctorName = doctor.getLastname() + " " + doctor.getFirstname() + " " + doctor.getFathername();

        UserJpa patient = userService.findUserJpaById(patientId);
        String patientName = patient.getLastname() + " " + patient.getFirstname() + " " + patient.getFathername();

        notificationService.addNotification(doctorId, "К Вам прикрепили нового пациента! ФИО: " + patientName + ".",
                LocalDateTime.now());

        notificationService.addNotification(patientId, "К Вам прикрепили нового врача! ФИО: " + doctorName + ".",
                LocalDateTime.now());
    }

    /**
     * Поиск всех пациентов.
     * @return пациенты
     */
    @RequestMapping(path="/doctorsList", method=RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public List<User> getDoctors() {
        List<UserJpa> userJpas = userService.findDoctors();
        return mapper.UserJpasToUsers(userJpas);
    }

    /**
     * Поиск всех врачей.
     * @return врачи
     */
    @RequestMapping(path="/patientsList", method=RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public List<User> getPatients() {
        List<UserJpa> userJpas = userService.findPatients();
        return mapper.UserJpasToUsers(userJpas);
    }

    /**
     * Изменение информации пользователя.
     */
    @PutMapping(path="/user", produces = "application/json;charset=UTF-8")
    public void changeUser(@RequestBody HashMap<String, String> data) {
        String id = data.get("id");

        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        String username = data.get("username");
        String firstname = data.get("firstname");
        String lastname = data.get("lastname");
        String email = data.get("email");
        LocalDate birthday = getDate(data.get("birthday"));
        String sex = data.get("sex");
        String fathername = data.get("fathername");

        UserJpa user = userService.findUserJpaById(id);

        if (username != null) {
            user.setUsername(username);
        }

        if (firstname != null) {
            user.setFirstname(firstname);
        }

        if (lastname != null) {
            user.setLastname(lastname);
        }

        if (email != null) {
            user.setEmail(email);
        }

        if (birthday != null) {
            user.setBirthday(birthday);
        }

        if (sex != null) {
            user.setSex(sex);
        }

        if (fathername != null) {
            user.setFathername(fathername);
        }

        String password = data.get("password");
        if (password != null) {
            user.setPasswordHash(String.valueOf(password.hashCode()));
        }

        userService.changeUser(user);
    }
}
