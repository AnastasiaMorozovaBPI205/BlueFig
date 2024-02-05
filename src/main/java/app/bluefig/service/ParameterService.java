package app.bluefig.service;

import app.bluefig.entity.ParameterJpa;
import app.bluefig.entity.RecommendationJpa;

import java.util.List;

public interface ParameterService {
    List<ParameterJpa> findParametersJpa();
}
