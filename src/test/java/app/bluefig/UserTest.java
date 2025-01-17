package app.bluefig;

import app.bluefig.controller.UserController;
import app.bluefig.entity.UserJpa;
import app.bluefig.mapper.MapStructMapper;
import app.bluefig.model.User;
import app.bluefig.service.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MapStructMapper.class)
@WebMvcTest(UserController.class)
public class UserTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl service;
    @MockBean
    private PatientHierarchyServiceImpl patientHierarchyService;
    @MockBean
    private QuestionaryAnswerServiceImpl questionaryAnswerService;
    @MockBean
    private DoctorParameterFillInServiceImpl doctorParameterFillInService;
    @MockBean
    private ModuleFillInServiceImpl moduleFillInService;
    @MockBean
    private QuestionaryServiceImpl questionaryService;
    @MockBean
    private RecommendationServiceImpl recommendationService;
    @MockBean
    private NotificationServiceImpl notificationService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private MapStructMapper mapper;

    @Test
    void registrationTest() throws Exception {
        String bodyRegistration = """
                {
                    "username": "Bobbob",
                    "firstname": "Bob",
                    "lastname": "Ross",
                    "fathername": "-",
                    "email": "bobbo@gmail.com",
                    "password": "1234",
                    "roleId": "49e6f33b-b4bb-11ee-8c0c-00f5f80cf8ae",
                    "birthday": "2024-03-13",
                    "sex": "male"
                }""";

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                .content(bodyRegistration)).andExpect(status().isOk());
    }

    @Test
    void authorizationTest() throws Exception {
        String bodyAuthorization = """
                {
                    "username": "Bobbo",
                    "password": "1234"
                }""";

        UserJpa user = getUserJpa();
        Mockito.when(this.service.findUserJpaByUsername("Bobbo")).
                thenReturn(user);
        Mockito.when(this.mapper.UserJpaToUser(user)).thenReturn(getUser());

        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                .content(bodyAuthorization))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void getUserByIdTest() throws Exception {
        UserJpa user = getUserJpa();
        Mockito.when(this.service.findUserJpaById("1")).thenReturn(user);
        Mockito.when(this.mapper.UserJpaToUser(user)).thenReturn(getUser());

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

    }

    @Test
    void deleteUserByIdTest() throws Exception {
        UserJpa user = getUserJpa();
        Mockito.when(this.service.findUserJpaById("1")).thenReturn(user);

        mockMvc.perform(delete("/user/1")).andExpect(status().isOk());
    }

    @Test
    void getDoctorsPatientsTest() throws Exception {
        List<UserJpa> users = getUserJpas();
        Mockito.when(this.service.findPatientsDoctorsJpa("1")).thenReturn(users);
        Mockito.when(this.mapper.UserJpasToUsers(users)).thenReturn(getUsers());

        mockMvc.perform(get("/doctorsList/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("2"));
    }

    @Test
    void getPatientsDoctorsTest() throws Exception {
        UserJpa userJpa = getUserJpa();
        List<UserJpa> users = List.of(userJpa);
        Mockito.when(this.service.findDoctorsPatientsJpa("2")).thenReturn(users);
        Mockito.when(this.mapper.UserJpasToUsers(users)).thenReturn(List.of(getUser()));

        mockMvc.perform(get("/patientsList/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void linkPatientToDoctorTest() throws Exception {
        UserJpa doctor = getUserJpa();
        Mockito.when(this.service.findUserJpaById("2")).thenReturn(doctor);
        UserJpa patient = getUserJpa();
        Mockito.when(this.service.findUserJpaById("1")).thenReturn(patient);

        mockMvc.perform(post("/linkPatient/1/2")).andExpect(status().isOk());
    }

    @Test
    void changeUserTest() throws Exception {
        String body = """
                {
                    "username": "Bobbo",
                    "id": "1"
                }""";

        UserJpa user = getUserJpa();
        Mockito.when(this.service.findUserJpaById("1")).thenReturn(user);

        mockMvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isOk());
    }

    private List<User> getUsers() {
        User user = new User();
        user.setUsername("Fobbo");
        user.setId("2");
        user.setBirthday(LocalDate.parse("2024-03-13"));
        user.setEmail("fobbo@gmail.com");
        user.setFirstname("Fob");
        user.setFathername("-");
        user.setLastname("Ross");
        user.setPasswordHash("4321");
        user.setRoleId("47b436f0-b4bb-11ee-8c0c-00f5f80cf8ae");
        user.setSex("male");

        return List.of(user);
    }

    private List<UserJpa> getUserJpas() {
        UserJpa user = new UserJpa();
        user.setUsername("Fobbo");
        user.setId("2");
        user.setBirthday(LocalDate.parse("2024-03-13"));
        user.setEmail("fobbo@gmail.com");
        user.setFirstname("Fob");
        user.setFathername("-");
        user.setLastname("Ross");
        user.setPasswordHash("4321");
        user.setRoleId("47b436f0-b4bb-11ee-8c0c-00f5f80cf8ae");
        user.setSex("male");

        return List.of(user);
    }

    private User getUser() {
        User user = new User();
        user.setUsername("Bobbo");
        user.setId("1");
        user.setBirthday(LocalDate.parse("2024-03-13"));
        user.setEmail("bobbo@gmail.com");
        user.setFirstname("Bob");
        user.setFathername("-");
        user.setLastname("Ross");
        user.setPasswordHash("$2a$10$kbpouTiVbGVC4OYFro4LyubH7yb.pTgjCo7yw1GTS5wKtPeQYO8gu");
        user.setRoleId("49e6f33b-b4bb-11ee-8c0c-00f5f80cf8ae");
        user.setSex("male");

        return user;
    }

    private UserJpa getUserJpa() {
        UserJpa user = new UserJpa();
        user.setUsername("Bobbo");
        user.setId("1");
        user.setBirthday(LocalDate.parse("2024-03-13"));
        user.setEmail("bobbo@gmail.com");
        user.setFirstname("Bob");
        user.setFathername("-");
        user.setLastname("Ross");
        user.setPasswordHash("$2a$10$kbpouTiVbGVC4OYFro4LyubH7yb.pTgjCo7yw1GTS5wKtPeQYO8gu");
        user.setRoleId("49e6f33b-b4bb-11ee-8c0c-00f5f80cf8ae");
        user.setSex("male");

        return user;
    }
}
