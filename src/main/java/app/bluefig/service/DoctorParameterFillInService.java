package app.bluefig.service;

import app.bluefig.entity.DoctorParameterFillInJpa;
import app.bluefig.model.DoctorParameterFillIn;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorParameterFillInService {
    void addDoctorParameterFillIn(String questionaryId, String parameterId, String value);
    void deleteDoctorParameterFillIn(String questionaryId);

    List<DoctorParameterFillInJpa> findDoctorParameterFillInJpas(String questionaryId);
}
