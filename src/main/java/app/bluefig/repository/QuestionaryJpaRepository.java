package app.bluefig.repository;

import app.bluefig.entity.QuestionaryJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionaryJpaRepository extends JpaRepository<QuestionaryJpa, String> {
    @Query(value = "insert into questionary (id, doctor_id, patient_id, module_id, frequency) " +
            "values (:id, :doctor_id, :patient_id, :module_id, :frequency)",
            nativeQuery = true)
    void addQuestionary(@Param("id") String id, @Param("doctor_id") String doctorId,
                        @Param("patient_id") String patientId, @Param("module_id") String moduleId,
                        @Param("frequency") int frequency);

    @Query(value = "select * from questionary where doctor_id = :doctor_id and patient_id = :patient_id",
            nativeQuery = true)
    List<QuestionaryJpa> findQuestionaryJpaByPatientDoctorIds(@Param("doctor_id") String doctorId,
                                                              @Param("patient_id") String patientId);

    @Query(value = "select * from questionary where patient_id = :patient_id",
            nativeQuery = true)
    List<QuestionaryJpa> findQuestionaryJpaByPatientId(@Param("patient_id") String patientId);

    @Query(value = "update questionary set frequency = :frequency where id = :id",
            nativeQuery = true)
    void updateQuestionaryFrequency(@Param("id") String id, @Param("frequency") int frequency);
}
