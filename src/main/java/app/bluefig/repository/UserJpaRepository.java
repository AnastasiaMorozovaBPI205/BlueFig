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
    @Query(value = "insert into sma_service.user (name, email, password, role, permission_code, birthday, sex, doctor_id)" +
            " values (:name, :email, :password, :role, :permission_code, :birthday, :sex, :doctor_id)", nativeQuery = true)
    void addUserJpa(@Param("name") String name, @Param("email") String email, @Param("password") String password,
                    @Param("role") String role, @Param("permission_code") String permissionCode,
                    @Param("birthday") LocalDate birthday, @Param("sex") String sex,
                    @Param("doctor_id") String doctorId);

    @Query(value = "select * from user where user.id = :id", nativeQuery = true)
    UserJpa findUserJpa(@Param("id") String id);

    @Query(value = "delete from sma_service.user where sma_service.user.id = :id", nativeQuery = true)
    void deleteUserJpa(@Param("id") String id);

    @Query(value = "select * from sma_service.user where sma_service.user.doctor_id = :doctor_id", nativeQuery = true)
    List<UserJpa> findDoctorsPatientsJpa(@Param("doctor_id") String doctorId);
}
