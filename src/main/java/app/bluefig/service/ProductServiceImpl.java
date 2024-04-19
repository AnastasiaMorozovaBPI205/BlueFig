package app.bluefig.service;

import app.bluefig.entity.ProductJpa;
import app.bluefig.repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductJpaRepository productJpaRepository;

    @Override
    public List<ProductJpa> findProductsInGroup(String productGroupId) {
        return productJpaRepository.findProductsInGroup(productGroupId);
    }

    @Override
    public int findProductEnergyByName(String name) {
        return productJpaRepository.findProductEnergyByName(name);
    }
}
