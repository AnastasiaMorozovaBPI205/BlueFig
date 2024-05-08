package app.bluefig.service;

import app.bluefig.entity.RecommendationJpa;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RecommendationService {
    List<RecommendationJpa> findRecommendationJpaByPatient(String patientId);
    List<RecommendationJpa> findRecommendationJpaByPatientDoctor(String patientId, String doctorId);
    void setDoctorIdUndefined(String doctorId);
    void addRecommendation(String patientId, String doctorId, LocalDateTime dateTime, String recommendation);
    void deletePatientRecommendations(String id);
    void setDoctorIdForUndefined(String doctorId, String patientId);

}
