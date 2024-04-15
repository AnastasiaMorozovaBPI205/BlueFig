package app.bluefig.repository;

import app.bluefig.entity.PatientHierarchyJpa;
import app.bluefig.entity.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientHierarchyJpaRepository extends JpaRepository<PatientHierarchyJpa, String> {
    @Query(value = "select user.id, user.username, user.firstname, user.lastname, user.email, user.password_hash, " +
            "user.role_id, user.birthday, user.sex, user.fathername from user " +
            "join patient_hierarchy on user.id = patient_hierarchy.patient_id  " +
            "order by patient_hierarchy.number desc", nativeQuery = true)
    List<UserJpa> findSortedPatientHierarchyJpas();

    @Query(value = "insert into patient_hierarchy (patient_id, number) values :patient_id, :number",
            nativeQuery = true)
    void addPatientToHierarchy(@Param("patient_id") String patientId, @Param("number") int number);
}
