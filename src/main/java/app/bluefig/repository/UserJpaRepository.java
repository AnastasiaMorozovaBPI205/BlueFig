package app.bluefig.repository;

import app.bluefig.entity.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface UserJpaRepository extends JpaRepository<UserJpa, String> {
    @Query(value = "insert into user (username, firstname, lastname, email, password_hash, role_id, birthday, sex, fathername)" +
            " values (:username, :firstname, :lastname, :email, :password_hash, :role_id, :birthday, :sex, :fathername)", nativeQuery = true)
    void addUserJpa(@Param("username") String username, @Param("firstname") String firstname,
                    @Param("lastname") String lastname, @Param("email") String email,
                    @Param("password_hash") String passwordHash, @Param("role_id") String roleId,
                    @Param("birthday") LocalDate birthday, @Param("sex") String sex, @Param("fathername") String fathername);

    @Query(value = "select * from user where user.id = :id", nativeQuery = true)
    UserJpa findUserJpaById(@Param("id") String id);

    @Query(value = "insert into doctor_watch (doctor_id, patient_id) values (:doctor_id, :patient_id)", nativeQuery = true)
    void linkPatientToDoctor(@Param("patient_id") String patientId, @Param("doctor_id") String doctorId);

    @Query(value = "select * from user where user.username = :username and user.password_hash = :password_hash", nativeQuery = true)
    UserJpa findUserJpaByUsernamePasswordHash(@Param("username") String username, @Param("password_hash") String passwordHash);

    @Query(value = "delete from user where user.id = :id", nativeQuery = true)
    void deleteUserJpa(@Param("id") String id);

    @Query(value = "select user.id, user.username, user.firstname, user.lastname, user.email, user.password_hash, " +
            "user.role_id, user.birthday, user.sex, user.fathername from user " +
            "join doctor_watch on user.id = doctor_watch.patient_id  " +
            "where doctor_watch.doctor_id = :doctor_id order by lastname", nativeQuery = true)
    List<UserJpa> findDoctorsPatientsJpa(@Param("doctor_id") String doctorId);

    @Query(value = "select user.id, user.username, user.firstname, user.lastname, user.email, user.password_hash, " +
            "user.role_id, user.birthday, user.sex, user.fathername from user " +
            "join doctor_watch on user.id = doctor_watch.doctor_id " +
            "where doctor_watch.patient_id = :patient_id order by lastname", nativeQuery = true)
    List<UserJpa> findPatientsDoctorsJpa(@Param("patient_id") String patientId);

    @Query(value = "select user.id, user.username, user.firstname, user.lastname, user.email, user.password_hash, " +
            "user.role_id, user.birthday, user.sex, user.fathername from user " +
            "join user_role on user.role_id = user_role.id " +
            "where user_role.name = 'doctor' order by lastname", nativeQuery = true)
    List<UserJpa> findDoctorsJpa();

    @Query(value = "select user.id, user.username, user.firstname, user.lastname, user.email, user.password_hash, " +
            "user.role_id, user.birthday, user.sex, user.fathername from user " +
            "join user_role on user.role_id = user_role.id " +
            "where user_role.name = 'patient' order by lastname", nativeQuery = true)
    List<UserJpa> findPatientsJpa();

    @Query(value = """
            select user.id, user.username, user.firstname, user.lastname, user.email, user.password_hash,\s
            user.role_id, user.birthday, user.sex, user.fathername from user\s
            join patient_hierarchy on user.id = patient_hierarchy.patient_id\s
            join doctor_watch on user.id = doctor_watch.patient_id where doctor_watch.doctor_id = :doctor_id\s
            order by patient_hierarchy.number desc""", nativeQuery = true)
    List<UserJpa> findSortedPatientHierarchyJpas(@Param("doctor_id") String doctorId);
}
