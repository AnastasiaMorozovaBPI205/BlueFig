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
    @Query(value = "insert into sma_service.user (username, firstname, lastname, email, password_hash, role_id, birthday, sex)" +
            " values (:username, :firstname, :lastname, :email, :password_hash, :role_id, :birthday, :sex)", nativeQuery = true)
    void addUserJpa(@Param("username") String username, @Param("firstname") String firstname,
                    @Param("lastname") String lastname, @Param("email") String email,
                    @Param("password_hash") String passwordHash, @Param("role_id") String roleId,
                    @Param("birthday") LocalDate birthday, @Param("sex") String sex);

    @Query(value = "select * from user where user.id = :id", nativeQuery = true)
    UserJpa findUserJpa(@Param("id") String id);

    @Query(value = "delete from sma_service.user where sma_service.user.id = :id", nativeQuery = true)
    void deleteUserJpa(@Param("id") String id);

    @Query(value = "select sma_service.user.id, sma_service.user.username, sma_service.user.firstname, " +
            "sma_service.user.lastname, sma_service.user.email, sma_service.user.password_hash, " +
            "sma_service.user.role_id, sma_service.user.birthday, sma_service.user.sex from sma_service.user \n" +
            "join sma_service.doctor_watch on sma_service.user.id = sma_service.doctor_watch.patient_id  \n" +
            "where sma_service.doctor_watch.doctor_id = :doctor_id;", nativeQuery = true)
    List<UserJpa> findDoctorsPatientsJpa(@Param("doctor_id") String doctorId);
}
