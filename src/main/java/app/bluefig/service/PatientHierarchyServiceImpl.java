package app.bluefig.service;

import app.bluefig.entity.PatientHierarchyJpa;
import app.bluefig.entity.UserJpa;
import app.bluefig.model.User;
import app.bluefig.repository.PatientHierarchyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientHierarchyServiceImpl implements PatientHierarchyService {
    @Autowired
    PatientHierarchyJpaRepository patientHierarchyJpaRepository;

    @Override
    public PatientHierarchyJpa findPatientHierarchyJpaById(String patientId) {
        return patientHierarchyJpaRepository.findById(patientId).orElse(null);
    }

    @Override
    public List<UserJpa> findSortedPatientHierarchyJpas() {
        return patientHierarchyJpaRepository.findSortedPatientHierarchyJpas();
    }

    @Override
    public void deletePatientFromHierarchyById(String patientId) {
        patientHierarchyJpaRepository.deleteById(patientId);
    }

    @Override
    public void addPatientToHierarchy(String patientId, int number) {
        patientHierarchyJpaRepository.addPatientToHierarchy(patientId, number);
    }

    @Override
    public void changePatientNumber(String patientId, int number) {
        patientHierarchyJpaRepository.changePatientNumber(patientId, number);
    }
}
