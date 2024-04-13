package app.bluefig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository  extends JpaRepository<String, String> {
    @Query(value = "select distinct product_group.name from product_group", nativeQuery = true)
    List<String> findProductGroups();

    @Query(value = """
            select product.name from product
            join product_group on product_group.id = product.group_id
            where product_group.name = :group_name""", nativeQuery = true)
    List<String> findProductsInGroup(@Param("group_name") String groupName);
}
