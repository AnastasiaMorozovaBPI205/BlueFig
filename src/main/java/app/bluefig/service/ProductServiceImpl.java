package app.bluefig.service;

import app.bluefig.repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductJpaRepository productJpaRepository;

    @Override
    public List<String> findProductGroups() {
        return productJpaRepository.findProductGroups();
    }

    @Override
    public List<String> findProductsInGroup(String groupName) {
        return productJpaRepository.findProductsInGroup(groupName);
    }
}
