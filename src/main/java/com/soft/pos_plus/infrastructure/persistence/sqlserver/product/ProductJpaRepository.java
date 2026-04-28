package com.soft.pos_plus.infrastructure.persistence.sqlserver.product;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soft.pos_plus.infrastructure.entities.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, UUID id);
}
