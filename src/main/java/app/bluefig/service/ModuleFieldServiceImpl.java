package app.bluefig.service;

import app.bluefig.entity.ModuleFieldJpa;
import app.bluefig.repository.ModuleFieldJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleFieldServiceImpl implements ModuleFieldService{
    @Autowired
    ModuleFieldJpaRepository moduleFieldJpaRepository;

    @Override
    public void addModuleField(String questionaryId, Integer orderNumber, Integer frequency, String parameterId) {
        moduleFieldJpaRepository.addModuleField(questionaryId, orderNumber, frequency, parameterId);
    }

    @Override
    public List<ModuleFieldJpa> findModuleFieldsBy(String questionaryId) {
        return moduleFieldJpaRepository.findModuleFieldsBy(questionaryId);
    }
}
