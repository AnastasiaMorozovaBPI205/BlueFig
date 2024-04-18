package app.bluefig.repository;

import app.bluefig.entity.FormulaJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormulaJpaRepository extends JpaRepository<FormulaJpa, String> {
    @Query(value = "select distinct nutrition_feature_value.name from nutrition_feature_value order by name", nativeQuery = true)
    List<String> findFormulaNames();

    @Query(value = "select nutrition_feature_value.value from nutrition_feature_value where name = :name", nativeQuery = true)
    String findFormulaByName(@Param("name") String name);
}
