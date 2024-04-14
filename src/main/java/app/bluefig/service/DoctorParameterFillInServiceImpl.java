package app.bluefig.service;

import app.bluefig.entity.DoctorParameterFillInJpa;
import app.bluefig.repository.DoctorParameterFillInJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorParameterFillInServiceImpl implements DoctorParameterFillInService {
    @Autowired
    DoctorParameterFillInJpaRepository doctorParameterFillInJpaRepository;

    @Override
    public void addDoctorParameterFillIn(String questionaryId, String parameterId, String value) {
        doctorParameterFillInJpaRepository.addDoctorParameterFillIn(questionaryId, parameterId, value);
    }

    @Override
    public List<DoctorParameterFillInJpa> findDoctorParameterFillInJpas(String questionaryId) {
        return doctorParameterFillInJpaRepository.findDoctorParameterFillIn(questionaryId);
    }
}
