package app.bluefig.service;

import app.bluefig.entity.DoctorParameterJpa;
import app.bluefig.repository.DoctorParameterJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorParameterServiceImpl implements DoctorParameterService {
    @Autowired
    DoctorParameterJpaRepository doctorParameterJpaRepository;

    @Override
    public List<DoctorParameterJpa> findDoctorParameterJpasByModuleId(String moduleId) {
        return doctorParameterJpaRepository.findDoctorParameterJpas(moduleId);
    }
}
