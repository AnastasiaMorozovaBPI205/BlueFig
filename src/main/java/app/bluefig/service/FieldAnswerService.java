package app.bluefig.service;

import app.bluefig.entity.FieldAnswerJpa;

import java.util.List;

public interface FieldAnswerService {
    void addFieldAnswer(String value, String fillInId, String fieldId);
    List<FieldAnswerJpa> findFieldAnswers(String fillInId);
}
