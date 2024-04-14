package app.bluefig.service;

import app.bluefig.entity.DoctorParameterJpa;

import java.util.List;

public interface DoctorParameterService {
    List<DoctorParameterJpa> findDoctorParameterJpasByModuleId(String moduleId);
}
