package app.bluefig.service;

import app.bluefig.entity.PatientHierarchyJpa;
import app.bluefig.entity.UserJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientHierarchyService {
    PatientHierarchyJpa findPatientHierarchyJpaById(String patientId);
    List<UserJpa> findSortedPatientHierarchyJpas();
    void deletePatientFromHierarchyById(String patientId);
    void addPatientToHierarchy(String patientId, int number);

}