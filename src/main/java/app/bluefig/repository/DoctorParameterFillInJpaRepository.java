package app.bluefig.repository;

import app.bluefig.entity.DoctorParameterFillInJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorParameterFillInJpaRepository extends JpaRepository<DoctorParameterFillInJpa, String> {
    @Query(value = "insert into questionary_doctor_parameters (questionary_id, parameter_id, value) " +
            "values (:questionary_id, :parameter_id, :value)", nativeQuery = true)
    void addDoctorParameterFillIn(@Param("questionary_id") String questionaryId,
                                  @Param("parameter_id")  String parameterId, @Param("value") String value);

    @Query(value = "select * from questionary_doctor_parameters where questionary_id = :questionary_id",
            nativeQuery = true)
    List<DoctorParameterFillInJpa> findDoctorParameterFillIn(@Param("questionary_id") String questionaryId);
}
