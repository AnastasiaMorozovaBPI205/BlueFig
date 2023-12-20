package app.bluefig.service;

import app.bluefig.entity.UserJpa;
import app.bluefig.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public void addUserList(String name, String email, String password, String role, String permissionCode,
                            LocalDate birthday, String sex, String doctorId) {
        //PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        userJpaRepository.addUserJpa(name, email, passwordEncoder.encode(password), role, permissionCode, birthday,
//                sex, doctorId);
        userJpaRepository.addUserJpa(name, email, password, role, permissionCode, birthday, sex, doctorId);
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
}