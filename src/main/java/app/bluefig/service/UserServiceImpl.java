package app.bluefig.service;

import app.bluefig.entity.UserJpa;
import app.bluefig.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public void addUserList(String username, String firstname, String lastname, String fathername,
                            String email, String passwordHash, String roleId,
                            LocalDate birthday, String sex) {
        userJpaRepository.addUserJpa(username, firstname, lastname, email, passwordHash, roleId, birthday, sex,
                fathername);
    }

    @Override
    public UserJpa findUserJpaById(String id) {
        return userJpaRepository.findUserJpaById(id);
    }

    @Override
    public UserJpa findUserJpaByUsernamePasswordHash(String username, String passwordHash) {
        return userJpaRepository.findUserJpaByUsernamePasswordHash(username, passwordHash);
    }

    @Override
    public void linkPatientToDoctor(String patientId, String doctorId) {
        userJpaRepository.linkPatientToDoctor(patientId, doctorId);
    }

    @Override
    public void deleteUserJpa(String id) {
        userJpaRepository.deleteUserJpa(id);
    }

    @Override
    public List<UserJpa> findDoctorsPatientsJpa(String doctorId) {
        return userJpaRepository.findDoctorsPatientsJpa(doctorId);
    }

    @Override
    public List<UserJpa> findPatientsDoctorsJpa(String patientId) {
        return userJpaRepository.findPatientsDoctorsJpa(patientId);
    }

    @Override
    public List<UserJpa> findDoctors() {
        return userJpaRepository.findDoctorsJpa();
    }

    @Override
    public List<UserJpa> findPatients() {
        return userJpaRepository.findPatientsJpa();
    }

    @Override
    public List<UserJpa> findSortedPatientHierarchyJpas(String doctorId) {
        return userJpaRepository.findSortedPatientHierarchyJpas(doctorId);
    }

    @Override
    public void changeUser(UserJpa userJpa) {
        userJpaRepository.save(userJpa);
    }

    @Override
    public void deleteDoctorFromWatch(String id) {
        userJpaRepository.deleteDoctorFromWatch(id);
    }
}