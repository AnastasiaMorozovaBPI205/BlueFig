package app.bluefig.repository;

import app.bluefig.entity.DoctorParameterJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorParameterJpaRepository  extends JpaRepository<DoctorParameterJpa, String> {
    @Query(value = "select * from doctor_parameters where module_id = :module_id", nativeQuery = true)
    List<DoctorParameterJpa> findDoctorParameterJpas(@Param("module_id") String moduleId);
}
