package app.bluefig.repository;

import app.bluefig.entity.ProductGroupJpa;
import app.bluefig.entity.ProductJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductGroupJpaRepository extends JpaRepository<ProductGroupJpa, String> {
    @Query(value = "select distinct * from product_group order by name", nativeQuery = true)
    List<ProductGroupJpa> findProductGroups();
}
