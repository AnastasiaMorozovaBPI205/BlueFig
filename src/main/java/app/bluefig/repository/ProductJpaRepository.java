package app.bluefig.repository;

import app.bluefig.entity.ProductJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpa, String> {
    @Query(value = "select * from product where group_id = :id order by name", nativeQuery = true)
    List<ProductJpa> findProductsInGroup(@Param("id") String productGroupId);

    @Query(value = """
            select product.id, product.name, product.group_id, product.protein, product.lipids, product.carbohydrates, product.energy
             from product\s
            join product_group on product.group_id = product_group.id\s
            order by  product.group_id\s""", nativeQuery = true)
    List<ProductJpa> findSortedProducts();

    @Query(value = "select product.energy from product where name = :name", nativeQuery = true)
    int findProductEnergyByName(@Param("name") String name);
}
