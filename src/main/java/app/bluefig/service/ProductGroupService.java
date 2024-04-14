package app.bluefig.service;

import app.bluefig.entity.ProductGroupJpa;

import java.util.List;

public interface ProductGroupService {
    List<ProductGroupJpa> findProductGroups();
}
