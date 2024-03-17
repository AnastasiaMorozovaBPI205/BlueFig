package app.bluefig.service;

import app.bluefig.entity.QuestionaryJpa;
import app.bluefig.repository.QuestionaryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionaryServiceImpl implements QuestionaryService {
    @Autowired
    QuestionaryJpaRepository questionaryJpaRepository;

    @Override
    public void addQuestionary(String id, String doctorId, String patientId, String moduleId, int frequency) {
        questionaryJpaRepository.addQuestionary(id, doctorId, patientId, moduleId, frequency);
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
}
