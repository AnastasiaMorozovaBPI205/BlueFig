package app.bluefig.repository;

import app.bluefig.entity.ModuleJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleJpaRepository extends JpaRepository<ModuleJpa, String> {
}
