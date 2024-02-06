package app.bluefig.service;

import app.bluefig.entity.ModuleFieldJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleFieldService {
    void addModuleField(String questionaryId, Integer orderNumber, Integer frequency, String parameterId);

    List<ModuleFieldJpa> findModuleFieldsBy(String questionaryId);
}
