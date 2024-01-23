package app.bluefig.repository;

import app.bluefig.entity.RecommendationJpa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendationJpaRepository {
    @Query(value = "select sma_service.doctor_recommendation.id, sma_service.doctor_recommendation.doctor_id, " +
            "sma_service.doctor_recommendation.patient_id, sma_service.doctor_recommendation.datetime, " +
            "sma_service.doctor_recommendation.recommendation, sma_service.user.firstname, sma_service.user.lastname, " +
            "sma_service.user.fathername " +
            "from sma_service.doctor_recommendation " +
            "join sma_service.user on sma_service.user.id = sma_service.doctor_recommendation.doctor_id " +
            "where sma_service.doctor_recommendation.patient_id = :patient_id", nativeQuery = true)
    List<RecommendationJpa> findRecommendationJpaByPatient(@Param("patient_id") String patientId);

    @Query(value = "insert into doctor_recommendation (doctor_id, patient_id, datetime, recommendation) " +
            "values (:doctor_id, :patient_id, :datetime, :recommendation);", nativeQuery = true)
    void addRecommendation(@Param("patient_id") String patientId, @Param("doctor_id") String doctorId,
                           @Param("datetime") LocalDateTime dateTime,
                           @Param("recommendation") String recommendation);
}
