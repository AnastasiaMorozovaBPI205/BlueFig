package app.bluefig.service;

import app.bluefig.entity.UserJpa;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    void addUserList(String username, String firstname, String lastname, String fathername,
                     String email, String passwordHash, String roleId,
                     LocalDate birthday, String sex);
    UserJpa findUserJpaById(String id);
    UserJpa findUserJpaByUsernamePasswordHash(String username, String passwordHash);
    void linkPatientToDoctor(String patientId, String doctorId);
    void deleteUserJpa(String id);
    List<UserJpa>  findDoctorsPatientsJpa(String doctorId);
    List<UserJpa>  findPatientsDoctorsJpa(String patientId);
    List<UserJpa>  findDoctors();
    List<UserJpa>  findPatients();
    List<UserJpa> findSortedPatientHierarchyJpas(String doctorId);
    void changeUser(UserJpa userJpa);
    void deleteDoctorFromWatch(String id);
}
