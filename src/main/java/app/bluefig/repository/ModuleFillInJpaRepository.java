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
    @Query(value = "insert into questionary_fillin (id, questionary_id, datetime, is_red) " +
            "values (:id, :questionary_id, :datetime, :is_red)", nativeQuery = true)
    void addModuleFillIn(@Param("id") String id, @Param("questionary_id") String questionaryId,
                   @Param("datetime") LocalDateTime datetime, @Param("is_red") boolean IsRed);

    @Query(value = "select questionary_fillin.id, questionary_fillin.questionary_id, questionary_fillin.datetime," +
            " questionary_fillin.is_red" +
            " from questionary_fillin join questionary on questionary.id = questionary_fillin.questionary_id" +
            " where questionary.doctor_id = :doctor_id and questionary.patient_id = :patient_id" +
            " order by questionary_fillin.datetime desc",
            nativeQuery = true)
    List<ModuleFillInJpa> findModulesFillInJpaByPatientDoctorIds(@Param("doctor_id") String doctorId,
                                                                 @Param("patient_id") String patientId);

    @Query(value = "select questionary_fillin.id, questionary_fillin.questionary_id, questionary_fillin.datetime," +
            "questionary_fillin.is_red" +
            " from questionary_fillin join questionary on questionary.id = questionary_fillin.questionary_id" +
            " where questionary.patient_id = :patient_id" +
            " order by questionary_fillin.datetime desc",
            nativeQuery = true)
    List<ModuleFillInJpa> findModulesFillInJpaByPatientId(@Param("patient_id") String patientId);

    @Query(value = """
            select questionary_fillin.id, questionary_fillin.questionary_id, questionary_fillin.datetime,\s
            questionary_fillin.is_red\s
            from questionary_fillin\s
            join questionary on questionary.id = questionary_fillin.questionary_id\s
            where questionary.patient_id = :patient_id and questionary.module_id = :module_id\s
            order by questionary_fillin.datetime""",
            nativeQuery = true)
    List<ModuleFillInJpa> findModulesFillInJpaByPatientIdModuleId(@Param("module_id") String moduleId,
                                                                 @Param("patient_id") String patientId);

    @Query(value = """
            select questionary_fillin.id, questionary_fillin.questionary_id, questionary_fillin.datetime,
            questionary_fillin.is_red\s
            from questionary_fillin join questionary on questionary.id = questionary_fillin.questionary_id\s
            where questionary.patient_id = :patient_id and questionary_fillin.questionary_id = :questionary_id\s
            order by questionary_fillin.datetime desc""",
            nativeQuery = true)
    List<ModuleFillInJpa> findModulesFillInJpaByPatientIdQuestionaryId(@Param("questionary_id") String questionaryId,
                                                                  @Param("patient_id") String patientId);
}
