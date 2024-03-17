package app.bluefig.service;

import app.bluefig.entity.QuestionaryAnswerJpa;
import app.bluefig.repository.QuestionaryAnswerJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionaryAnswerServiceImpl implements QuestionaryAnswerService {
    @Autowired
    QuestionaryAnswerJpaRepository questionaryAnswerJpaRepository;

    @Override
    public void addFieldAnswer(String value, String fillInId, String parameterId) {
        questionaryAnswerJpaRepository.addFieldAnswer(value, fillInId, parameterId);
    }

    @Override
    public List<QuestionaryAnswerJpa> findFieldAnswers(String fillInId) {
        return questionaryAnswerJpaRepository.findFieldAnswers(fillInId);
    }
}
