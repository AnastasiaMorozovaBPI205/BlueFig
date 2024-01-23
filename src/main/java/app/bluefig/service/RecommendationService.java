package app.bluefig.service;

import app.bluefig.entity.RecommendationJpa;

import java.time.LocalDateTime;
import java.util.List;

public interface RecommendationService {
    List<RecommendationJpa> findRecommendationJpaByPatient(String patientId);

    void addRecommendation(String patientId, String doctorId, LocalDateTime dateTime, String recommendation);
}
