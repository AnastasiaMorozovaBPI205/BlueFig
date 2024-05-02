package app.bluefig.service;

import app.bluefig.entity.ModuleFillInJpa;
import app.bluefig.repository.ModuleFillInJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ModuleFillInServiceImpl implements ModuleFillInService{
    @Autowired
    ModuleFillInJpaRepository moduleFillInJpaRepository;

    @Override
    public void addModuleFillIn(String id, String questionaryId, LocalDateTime datetime, boolean isRed) {
        moduleFillInJpaRepository.addModuleFillIn(id, questionaryId, datetime, isRed);
    }

    @Override
    public List<ModuleFillInJpa> findModulesFillInJpaByPatientDoctorIds(String doctorId, String patientId) {
        return moduleFillInJpaRepository.findModulesFillInJpaByPatientDoctorIds(doctorId, patientId);
    }

    @Override
    public List<ModuleFillInJpa> findModulesFillInJpaByPatientIdModuleId(String moduleId, String patientId) {
        return moduleFillInJpaRepository.findModulesFillInJpaByPatientIdModuleId(moduleId, patientId);
    }

    @Override
    public List<ModuleFillInJpa> findModulesFillInJpaByPatientIdQuestionaryId(String questionaryId, String patientId) {
        return moduleFillInJpaRepository.findModulesFillInJpaByPatientIdQuestionaryId(questionaryId, patientId);
    }

    @Override
    public List<ModuleFillInJpa> findModulesFillInJpaByPatientId(String patientId) {
        return moduleFillInJpaRepository.findModulesFillInJpaByPatientId(patientId);
    }

    @Override
    public void deleteFillInById(String id) {
        moduleFillInJpaRepository.deleteById(id);
    }
}
