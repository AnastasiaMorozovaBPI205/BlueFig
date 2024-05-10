package app.bluefig.service;

import app.bluefig.entity.QuestionaryJpa;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionaryService {
    void addQuestionary(String id, String doctorId, String patientId, String moduleId, int frequency, LocalDateTime dateTime);
    List<QuestionaryJpa> findQuestionaryJpaByPatientDoctorIds(String doctorId, String patientId);
    List<QuestionaryJpa> findQuestionaryJpaByPatientIdAll(String patientId);
    List<QuestionaryJpa> findQuestionaryJpaByPatientDoctorIdsAll(String doctorId, String patientId);
    void setQuestionaryInactive(String questionaryId);
    void updateQuestionaryFrequency(String id, int frequency);
    List<QuestionaryJpa> findQuestionaryJpaByPatientId(String patientId);
    QuestionaryJpa findQuestionaryById(String questionaryId);
    String findQuestionaryByPatientIdModuleId(String patientId, String moduleId);
    void setDoctorIdUndefined(String doctorId);
    void setDoctorIdForUndefined(String doctorId, String patientId);
    void deleteQuestionaryById(String id);
}
