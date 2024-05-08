package app.bluefig.repository;

import app.bluefig.entity.QuestionaryJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionaryJpaRepository extends JpaRepository<QuestionaryJpa, String> {
    @Query(value = "insert into questionary (id, doctor_id, patient_id, module_id, frequency, datetime) " +
            "values (:id, :doctor_id, :patient_id, :module_id, :frequency, :datetime)", nativeQuery = true)
    void addQuestionary(@Param("id") String id, @Param("doctor_id") String doctorId,
                        @Param("patient_id") String patientId, @Param("module_id") String moduleId,
                        @Param("frequency") int frequency, @Param("datetime") LocalDateTime dateTime);

    @Query(value = "select * from questionary where doctor_id = :doctor_id and patient_id = :patient_id and is_active = 1",
            nativeQuery = true)
    List<QuestionaryJpa> findQuestionaryJpaByPatientDoctorIds(@Param("doctor_id") String doctorId,
                                                              @Param("patient_id") String patientId);
    @Query(value = "select * from questionary where doctor_id = :doctor_id and patient_id = :patient_id",
            nativeQuery = true)
    List<QuestionaryJpa> findQuestionaryJpaByPatientDoctorIdsAll(@Param("doctor_id") String doctorId,
                                                              @Param("patient_id") String patientId);

    @Query(value = "select * from questionary where patient_id = :patient_id and is_active = 1 order by datetime desc", nativeQuery = true)
    List<QuestionaryJpa> findQuestionaryJpaByPatientId(@Param("patient_id") String patientId);

    @Query(value = "select * from questionary where patient_id = :patient_id order by datetime desc", nativeQuery = true)
    List<QuestionaryJpa> findQuestionaryJpaByPatientIdAll(@Param("patient_id") String patientId);

    @Query(value = "update questionary set frequency = :frequency where id = :id", nativeQuery = true)
    void updateQuestionaryFrequency(@Param("id") String id, @Param("frequency") int frequency);

    @Query(value = "select * from questionary where id = :id", nativeQuery = true)
    QuestionaryJpa findQuestionaryById(@Param("id") String questionaryId);

    @Query(value = "select questionary.id from questionary where patient_id = :patient_id and" +
            " module_id = :module_id order by datetime desc", nativeQuery = true)
    List<String> findQuestionaryByPatientIdModuleId(@Param("patient_id") String patientId,
                                              @Param("module_id") String moduleId);
    @Query(value = "update questionary set doctor_id = 'undefined' where doctor_id = :doctor_id", nativeQuery = true)
    void setDoctorIdUndefined(@Param("doctor_id") String doctorId);
    @Query(value = "update questionary set is_active = 0 where id = :id", nativeQuery = true)
    void setQuestionaryInactive(@Param("id") String questionaryId);
}
