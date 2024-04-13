package app.bluefig.service;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductService {
    List<String> findProductGroups();
    List<String> findProductsInGroup(String groupName);

}
