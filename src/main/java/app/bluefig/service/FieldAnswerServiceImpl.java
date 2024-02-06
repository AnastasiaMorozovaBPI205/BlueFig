package app.bluefig.service;

import app.bluefig.entity.FieldAnswerJpa;
import app.bluefig.repository.FieldAnswerJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldAnswerServiceImpl implements FieldAnswerService{
    @Autowired
    FieldAnswerJpaRepository fieldAnswerJpaRepository;

    @Override
    public void addFieldAnswer(String value, String fillInId, String fieldId) {
        fieldAnswerJpaRepository.addFieldAnswer(value, fillInId, fieldId);
    }

    @Override
    public List<FieldAnswerJpa> findFieldAnswers(String fillInId) {
        return fieldAnswerJpaRepository.findFieldAnswers(fillInId);
    }
}
