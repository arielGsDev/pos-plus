package com.soft.pos_plus.application.mappers;

import com.soft.pos_plus.application.dtos.CreateProductRequest;
import com.soft.pos_plus.application.dtos.ProductResponse;
import com.soft.pos_plus.application.dtos.UpdateProductRequest;
import com.soft.pos_plus.domain.entities.Product;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ProductApplicationMapper {

    public Product toDomain(CreateProductRequest request) {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setActive(request.isActive());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }

    public void applyUpdate(Product product, UpdateProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }
        product.setUpdatedAt(LocalDateTime.now());
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setSku(product.getSku());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCategory(product.getCategory());
        response.setActive(product.isActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}
