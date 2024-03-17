package app.bluefig.service;

import app.bluefig.entity.ModuleJpa;
import app.bluefig.repository.ModuleJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    ModuleJpaRepository moduleJpaRepository;

    @Override
    public List<ModuleJpa> findModulesJpa() {
        return moduleJpaRepository.findAll();
    }
}
