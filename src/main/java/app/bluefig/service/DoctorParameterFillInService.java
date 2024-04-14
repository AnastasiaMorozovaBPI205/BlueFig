package app.bluefig.service;

import app.bluefig.entity.DoctorParameterFillInJpa;
import app.bluefig.model.DoctorParameterFillIn;

import java.util.List;

public interface DoctorParameterFillInService {
    void addDoctorParameterFillIn(String questionaryId, String parameterId, String value);

    List<DoctorParameterFillInJpa> findDoctorParameterFillInJpas(String questionaryId);
}
