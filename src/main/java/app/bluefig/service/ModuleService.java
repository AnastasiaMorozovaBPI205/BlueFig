package app.bluefig.service;

import app.bluefig.entity.ModuleJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleService {
    List<ModuleJpa> findModulesJpa();
    String getQuestionaryModuleName(String questionaryId);
}
