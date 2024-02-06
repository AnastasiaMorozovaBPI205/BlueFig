package app.bluefig.repository;

import app.bluefig.entity.ModuleFieldJpa;
import app.bluefig.entity.ModuleJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleJpaRepository extends JpaRepository<ModuleJpa, String> {
    @Query(value = "insert into questionary (id, doctor_id, patient_id) values (:id, :doctor_id, :patient_id);",
            nativeQuery = true)
    void addModule(@Param("id") String id, @Param("doctor_id") String doctorId,
                        @Param("patient_id") String patientId);

    @Query(value = "select * from questionary where doctor_id = :doctor_id and patient_id = :patient_id;",
            nativeQuery = true)
    List<ModuleJpa> findModulesJpaByPatientDoctorIds(@Param("doctor_id") String doctorId,
                                                     @Param("patient_id") String patientId);
}
