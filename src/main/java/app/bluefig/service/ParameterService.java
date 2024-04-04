package app.bluefig.service;

import app.bluefig.entity.ParameterJpa;

import java.util.List;
import java.util.Optional;

public interface ParameterService {
    List<ParameterJpa> findParametersJpa();
    ParameterJpa findParameterJpaById(String parameterId);
}
