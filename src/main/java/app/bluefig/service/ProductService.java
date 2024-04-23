package app.bluefig.service;

import app.bluefig.entity.ProductJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductService {
    List<ProductJpa> findProductsInGroup(String groupId);
    int findProductEnergyByName(String name);
    List<ProductJpa> findSortedProducts();

}
