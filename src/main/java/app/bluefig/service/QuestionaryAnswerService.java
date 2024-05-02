package app.bluefig.service;

import app.bluefig.entity.QuestionaryAnswerJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionaryAnswerService {
    void addFieldAnswer(String value, String fillInId, String parameterId);
    List<QuestionaryAnswerJpa> findFieldAnswers(String fillInId);
    void deleteQuestionaryAnswers(String fillInId);
}
