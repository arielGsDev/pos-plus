package com.soft.pos_plus.domain.repositories;

import com.soft.pos_plus.domain.entities.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAll();

    Product update(Product product);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, UUID id);
}
