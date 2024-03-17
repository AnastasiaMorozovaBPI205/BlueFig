package app.bluefig.repository;

import app.bluefig.entity.GastroLabelJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GastroLabelJpaRepository extends JpaRepository<GastroLabelJpa, String> {
    @Query(value = "SELECT labels.id, labels.name, labels.is_red_flag from labels " +
            "join gastro_parameters_labels gpl on labels.id = gpl.label_id " +
            "where gpl.parameter_id = :parameter_id", nativeQuery = true)
    List<GastroLabelJpa> findGastroLabelByParameter(@Param("parameter_id") String parameterId);
}
