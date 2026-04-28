package com.soft.pos_plus.infrastructure.persistence.sqlserver;

import com.soft.pos_plus.domain.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class SqlServerProductMapper {

    public SqlServerProductEntity toEntity(Product product) {
        SqlServerProductEntity entity = new SqlServerProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setSku(product.getSku());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        entity.setCategory(product.getCategory());
        entity.setActive(product.isActive());
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());
        return entity;
    }

    public Product toDomain(SqlServerProductEntity entity) {
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setSku(entity.getSku());
        product.setPrice(entity.getPrice());
        product.setStock(entity.getStock());
        product.setCategory(entity.getCategory());
        product.setActive(entity.isActive());
        product.setCreatedAt(entity.getCreatedAt());
        product.setUpdatedAt(entity.getUpdatedAt());
        return product;
    }
}
