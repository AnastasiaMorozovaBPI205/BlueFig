package app.bluefig.repository;

import app.bluefig.entity.ModuleJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleJpaRepository extends JpaRepository<ModuleJpa, String> {
    @Query(value = "select modules.name from modules join questionary on questionary.id = :id " +
            "where questionary.module_id = modules.id", nativeQuery = true)
    String getQuestionaryModuleName(@Param("id") String questionaryId);
}
