package app.bluefig.service;

import app.bluefig.entity.DoctorParameterLabelJpa;
import app.bluefig.repository.DoctorParameterLabelJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorParameterLabelServiceImpl implements DoctorParameterLabelService {
    @Autowired
    DoctorParameterLabelJpaRepository doctorParameterLabelJpaRepository;

    @Override
    public List<DoctorParameterLabelJpa> findDoctorParameterJpas(String parameterId) {
        return doctorParameterLabelJpaRepository.findDoctorParameterJpas(parameterId);
    }
}
