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
        //PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        userJpaRepository.addUserJpa(name, email, passwordEncoder.encode(password), role, permissionCode, birthday,
//                sex, doctorId);
        userJpaRepository.addUserJpa(username, firstname, lastname, email, passwordHash, roleId, birthday, sex,
                fathername);
    }

    @Override
    public UserJpa findUserJpa(String id) {
        return userJpaRepository.findUserJpa(id);
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
}