package app.bluefig.service;

import app.bluefig.entity.ParameterJpa;
import app.bluefig.repository.ParameterJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParameterServiceImpl implements ParameterService {
    @Autowired
    private ParameterJpaRepository parameterJpaRepository;

    @Override
    public List<ParameterJpa> findParametersJpa() {
        return parameterJpaRepository.findAll();
    }

    @Override
    public ParameterJpa findParameterJpaById(String parameterId) {
        Optional<ParameterJpa> parameterJpa = parameterJpaRepository.findById(parameterId);
        return parameterJpa.orElse(null);
    }
}
