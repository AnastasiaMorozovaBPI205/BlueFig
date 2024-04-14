package app.bluefig.repository;

import app.bluefig.entity.DoctorParameterJpa;
import app.bluefig.entity.DoctorParameterLabelJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorParameterLabelJpaRepository extends JpaRepository<DoctorParameterLabelJpa, String> {
    @Query(value = "select * from doctor_parameters_labels where parameter_id = :parameter_id", nativeQuery = true)
    List<DoctorParameterLabelJpa> findDoctorParameterJpas(@Param("parameter_id") String parameterId);
}
