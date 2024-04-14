package app.bluefig.service;

import app.bluefig.entity.DoctorParameterLabelJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorParameterLabelService {
    List<DoctorParameterLabelJpa> findDoctorParameterJpas(String parameterId);

}
