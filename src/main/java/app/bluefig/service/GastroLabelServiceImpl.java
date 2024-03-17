package app.bluefig.service;

import app.bluefig.entity.GastroLabelJpa;
import app.bluefig.repository.GastroLabelJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GastroLabelServiceImpl implements GastroLabelService{
    @Autowired
    GastroLabelJpaRepository gastroLabelJpaRepository;

    @Override
    public List<GastroLabelJpa> findGastroLabelByParameter(String parameterId) {
        return gastroLabelJpaRepository.findGastroLabelByParameter(parameterId);
    }
}
