package com.soft.pos_plus.infrastructure.persistence.sqlserver;

import org.springframework.stereotype.Component;

import com.soft.pos_plus.domain.entities.Product;
import com.soft.pos_plus.infrastructure.entities.ProductEntity;

@Component
public class ProductMapper {

    public ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
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

    public Product toDomain(ProductEntity entity) {
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
