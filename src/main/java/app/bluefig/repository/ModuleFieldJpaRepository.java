package app.bluefig.repository;

import app.bluefig.entity.ModuleFieldJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleFieldJpaRepository extends JpaRepository<ModuleFieldJpa, String> {
    @Query(value = "insert into questionary_field (questionary_id, order_number, frequency, parameter_id) " +
            "values (:questionary_id, :order_number, :frequency, :parameter_id);", nativeQuery = true)
    void addModuleField(@Param("questionary_id") String questionaryId, @Param("order_number") Integer orderNumber,
                           @Param("frequency") Integer frequency,
                           @Param("parameter_id") String parameterId);

    @Query(value = "select * from questionary_field where questionary_id = :questionary_id;", nativeQuery = true)
    List<ModuleFieldJpa> findModuleFieldsBy(@Param("questionary_id") String questionaryId);
}
