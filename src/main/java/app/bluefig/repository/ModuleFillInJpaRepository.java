package app.bluefig.repository;

import app.bluefig.entity.ModuleFillInJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ModuleFillInJpaRepository extends JpaRepository<ModuleFillInJpa, String> {
    @Query(value = "insert into questionary_fillin (id, questionary_id, datetime) " +
            "values (:id, :questionary_id, :datetime)", nativeQuery = true)
    void addModuleFillIn(@Param("id") String id, @Param("questionary_id") String questionaryId,
                   @Param("datetime") LocalDateTime datetime);

    @Query(value = "select questionary_fillin.id, questionary_fillin.questionary_id, questionary_fillin.datetime" +
            " from questionary_fillin join questionary on questionary.id = questionary_fillin.questionary_id" +
            " where questionary.doctor_id = :doctor_id and questionary.patient_id = :patient_id",
            nativeQuery = true)
    List<ModuleFillInJpa> findModulesFillInJpaByPatientDoctorIds(@Param("doctor_id") String doctorId,
                                                     @Param("patient_id") String patientId);
}
