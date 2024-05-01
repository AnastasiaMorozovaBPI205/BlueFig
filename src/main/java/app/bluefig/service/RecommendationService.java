package app.bluefig.service;

import app.bluefig.entity.RecommendationJpa;

import java.time.LocalDateTime;
import java.util.List;

public interface RecommendationService {
    List<RecommendationJpa> findRecommendationJpaByPatient(String patientId);
    List<RecommendationJpa> findRecommendationJpaByPatientDoctor(String patientId, String doctorId);

    void addRecommendation(String patientId, String doctorId, LocalDateTime dateTime, String recommendation);
}
