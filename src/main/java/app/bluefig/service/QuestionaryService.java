package app.bluefig.service;

import app.bluefig.entity.QuestionaryJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionaryService {
    void addQuestionary(String id, String doctorId, String patientId, String moduleId, int frequency);
    List<QuestionaryJpa> findQuestionaryJpaByPatientDoctorIds(String doctorId, String patientId);
    void deleteQuestionaryById (String questionaryId);
    void updateQuestionaryFrequency(String id, int frequency);
    List<QuestionaryJpa> findQuestionaryJpaByPatientId(String patientId);
    QuestionaryJpa findQuestionaryById(String questionaryId);
    String findQuestionaryByPatientIdModuleId(String patientId, String moduleId);

}
