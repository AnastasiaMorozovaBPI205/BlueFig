package app.bluefig.repository;

import app.bluefig.entity.RecommendationJpa;
import app.bluefig.entity.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendationJpaRepository extends JpaRepository<RecommendationJpa, String>  {
    @Query(value = "select doctor_recommendation.id, doctor_recommendation.doctor_id, " +
            "doctor_recommendation.patient_id, doctor_recommendation.datetime, " +
            "doctor_recommendation.recommendation, user.firstname, user.lastname, user.fathername " +
            "from doctor_recommendation " +
            "join user on user.id = doctor_recommendation.doctor_id " +
            "where doctor_recommendation.patient_id = :patient_id " +
            "order by doctor_recommendation.datetime desc", nativeQuery = true)
    List<RecommendationJpa> findRecommendationJpaByPatient(@Param("patient_id") String patientId);

    @Query(value = "select doctor_recommendation.id, doctor_recommendation.doctor_id, " +
            "doctor_recommendation.patient_id, doctor_recommendation.datetime, " +
            "doctor_recommendation.recommendation, user.firstname, user.lastname, user.fathername " +
            "from doctor_recommendation " +
            "join user on user.id = doctor_recommendation.doctor_id " +
            "where doctor_recommendation.patient_id = :patient_id and doctor_recommendation.doctor_id = :doctor_id " +
            "order by doctor_recommendation.datetime desc", nativeQuery = true)
    List<RecommendationJpa> findRecommendationJpaByPatientDoctor(@Param("patient_id") String patientId,
                                                           @Param("doctor_id") String doctorId);

    @Query(value = "insert into doctor_recommendation (doctor_id, patient_id, datetime, recommendation) " +
            "values (:doctor_id, :patient_id, :datetime, :recommendation)", nativeQuery = true)
    void addRecommendation(@Param("patient_id") String patientId, @Param("doctor_id") String doctorId,
                           @Param("datetime") LocalDateTime dateTime,
                           @Param("recommendation") String recommendation);
}
