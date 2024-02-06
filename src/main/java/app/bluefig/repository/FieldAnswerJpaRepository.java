package app.bluefig.repository;

import app.bluefig.entity.FieldAnswerJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldAnswerJpaRepository extends JpaRepository<FieldAnswerJpa, String> {
    @Query(value = "insert into questionary_answer (value, fillin_id, field_id) " +
            "values (:value, :fillin_id, :field_id);", nativeQuery = true)
    void addFieldAnswer(@Param("value") String value, @Param("fillin_id") String fillInId,
                        @Param("field_id") String fieldId);

    @Query(value = "select * from questionary_answer where fillin_id = :fillin_id;", nativeQuery = true)
    List<FieldAnswerJpa> findFieldAnswers(@Param("fillin_id") String fillInId);
}
