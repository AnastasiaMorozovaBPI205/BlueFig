package app.bluefig.controller;

import app.bluefig.mapper.MapStructMapper;
import app.bluefig.entity.RecommendationJpa;
import app.bluefig.model.Recommendation;
import app.bluefig.service.NotificationServiceImpl;
import app.bluefig.service.PatientHierarchyServiceImpl;
import app.bluefig.service.RecommendationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class RecommendationController {
    @Autowired
    RecommendationServiceImpl recommendationService;

    @Autowired
    NotificationServiceImpl notificationService;

    @Autowired
    PatientHierarchyServiceImpl patientHierarchyService;

    @Autowired
    private MapStructMapper mapper;

    /**
     * Добавление рекомендации для пациента.
     * @param data данные
     */
    @PostMapping(path = "/recommendation", produces = "application/json;charset=UTF-8")
    public void addRecommendation(@RequestBody HashMap<String, String> data) {
        String patientId = data.get("patientId");
        String doctorId = data.get("doctorId");
        String recommendation = data.get("recommendation");

        if (patientId == null || doctorId == null || recommendation == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        recommendationService.addRecommendation(patientId, doctorId, LocalDateTime.now(), recommendation);
        patientHierarchyService.deletePatientFromHierarchyById(patientId);
        notificationService.addNotification(patientId, "У вас новая рекомендация от врача!", LocalDateTime.now());
        System.out.println("recommendation added successfully");
    }

    /**
     * Поиск рекомендаций по id пациента.
     * @param patientId пациента
     * @return список рекомендаций
     */
    @RequestMapping(path="/recommendation/{patientId}", method=RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public List<Recommendation> getRecommendationByPatient(@PathVariable String patientId) {
        List<RecommendationJpa> recommendationJpas = recommendationService.findRecommendationJpaByPatient(patientId);
        return mapper.RecommendationJpasToRecommendations(recommendationJpas);
    }

    /**
     * Поиск рекомендаций по id пациента и врача.
     * @param patientId пациента
     * @return список рекомендаций
     */
    @RequestMapping(path="/recommendation/{patientId}/{doctorId}", method=RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public List<Recommendation> getRecommendationByPatientDoctor(@PathVariable String patientId,
                                                           @PathVariable String doctorId) {
        List<RecommendationJpa> recommendationJpas = recommendationService.findRecommendationJpaByPatientDoctor(
                patientId, doctorId);
        return mapper.RecommendationJpasToRecommendations(recommendationJpas);
    }
}
