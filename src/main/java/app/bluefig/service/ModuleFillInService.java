package app.bluefig.service;

import app.bluefig.entity.ModuleFillInJpa;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ModuleFillInService {
    void addModuleFillIn(String id, String questionaryId,LocalDateTime datetime);
    List<ModuleFillInJpa> findModulesFillInJpaByPatientDoctorIds(String doctorId, String patientId);
    List<ModuleFillInJpa> findModulesFillInJpaByPatientIdModuleId(String moduleId, String patientId);
}
