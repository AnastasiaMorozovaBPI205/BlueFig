package app.bluefig.service;

import app.bluefig.entity.ModuleJpa;

import java.util.List;

public interface ModuleService {
    List<ModuleJpa> findModulesJpa();
}
