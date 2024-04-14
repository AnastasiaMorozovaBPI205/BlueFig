package app.bluefig.service;

import app.bluefig.entity.ProductGroupJpa;
import app.bluefig.repository.ProductGroupJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductGroupServiceImpl implements ProductGroupService {
    @Autowired
    ProductGroupJpaRepository productGroupJpaRepository;

    @Override
    public List<ProductGroupJpa> findProductGroups() {
        return productGroupJpaRepository.findProductGroups();
    }
}
