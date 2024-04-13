package app.bluefig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormulaJpaRepository extends JpaRepository<String, String> {
    @Query(value = "select distinct nutrition_feature_value.name from nutrition_feature_value", nativeQuery = true)
    List<String> findFormulaNames();
}
