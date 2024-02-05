package app.bluefig.repository;

import app.bluefig.entity.ParameterJpa;
import app.bluefig.entity.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterJpaRepository extends JpaRepository<ParameterJpa, String> {

}
