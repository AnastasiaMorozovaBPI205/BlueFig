package app.bluefig.service;

import app.bluefig.entity.QuestionaryJpa;
import app.bluefig.repository.QuestionaryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionaryServiceImpl implements QuestionaryService {
    @Autowired
    QuestionaryJpaRepository questionaryJpaRepository;

    @Override
    public void addQuestionary(String id, String doctorId, String patientId, String moduleId, int frequency, LocalDateTime datetime) {
        questionaryJpaRepository.addQuestionary(id, doctorId, patientId, moduleId, frequency, datetime);
    }

    @Override
    public List<QuestionaryJpa> findQuestionaryJpaByPatientDoctorIds(String doctorId, String patientId) {
        return questionaryJpaRepository.findQuestionaryJpaByPatientDoctorIds(doctorId, patientId);
    }

    @Override
    public void deleteQuestionaryById(String questionaryId) {
        questionaryJpaRepository.deleteById(questionaryId);
    }

    @Override
    public void updateQuestionaryFrequency(String id, int frequency) {
        questionaryJpaRepository.updateQuestionaryFrequency(id, frequency);
    }

    @Override
    public List<QuestionaryJpa> findQuestionaryJpaByPatientId(String patientId) {
        return questionaryJpaRepository.findQuestionaryJpaByPatientId(patientId);
    }

    @Override
    public QuestionaryJpa findQuestionaryById(String questionaryId) {
        return questionaryJpaRepository.findQuestionaryById(questionaryId);
    }

    @Override
    public String findQuestionaryByPatientIdModuleId(String patientId, String moduleId) {
        return questionaryJpaRepository.findQuestionaryByPatientIdModuleId(patientId, moduleId);
    }
}
