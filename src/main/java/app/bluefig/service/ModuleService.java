package app.bluefig.service;

import app.bluefig.entity.ModuleJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleService {
    void addModule(String id, String doctorId, String patientId);

    List<ModuleJpa> findModulesJpaByPatientDoctorIds(String doctorId, String patientId);
}
