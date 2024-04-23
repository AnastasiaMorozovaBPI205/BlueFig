package app.bluefig.repository;

import app.bluefig.entity.PatientHierarchyJpa;
import app.bluefig.entity.UserJpa;
import app.bluefig.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientHierarchyJpaRepository extends JpaRepository<PatientHierarchyJpa, String> {
    @Query(value = "insert into patient_hierarchy (patient_id, number) values (:patient_id, :number)",
            nativeQuery = true)
    void addPatientToHierarchy(@Param("patient_id") String patientId, @Param("number") int number);

    @Query(value = "update patient_hierarchy set number = :number where patient_id = :patient_id",
            nativeQuery = true)
    void changePatientNumber(@Param("patient_id") String patientId, @Param("number") int number);
}
