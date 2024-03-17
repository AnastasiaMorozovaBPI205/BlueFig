package app.bluefig.service;

import app.bluefig.entity.QuestionaryJpa;

import java.util.List;

public interface QuestionaryService {
    void addQuestionary(String id, String doctorId, String patientId, String moduleId, int frequency);

    List<QuestionaryJpa> findQuestionaryJpaByPatientDoctorIds(String doctorId, String patientId);
}
