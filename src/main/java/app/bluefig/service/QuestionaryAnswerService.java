package app.bluefig.service;

import app.bluefig.entity.QuestionaryAnswerJpa;

import java.util.List;

public interface QuestionaryAnswerService {
    void addFieldAnswer(String value, String fillInId, String parameterId);
    List<QuestionaryAnswerJpa> findFieldAnswers(String fillInId);
}
