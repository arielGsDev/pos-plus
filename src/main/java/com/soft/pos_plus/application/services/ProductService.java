package com.soft.pos_plus.application.services;

import com.soft.pos_plus.application.dtos.CreateProductRequest;
import com.soft.pos_plus.application.dtos.ProductResponse;
import com.soft.pos_plus.application.dtos.UpdateProductRequest;
import com.soft.pos_plus.application.exception.BadRequestException;
import com.soft.pos_plus.application.exception.ConflictException;
import com.soft.pos_plus.application.exception.NotFoundException;
import com.soft.pos_plus.application.mappers.ProductApplicationMapper;
import com.soft.pos_plus.domain.entities.Product;
import com.soft.pos_plus.domain.repositories.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductApplicationMapper productApplicationMapper;

    public ProductService(ProductRepository productRepository, ProductApplicationMapper productApplicationMapper) {
        this.productRepository = productRepository;
        this.productApplicationMapper = productApplicationMapper;
    }

    public ProductResponse create(CreateProductRequest request) {
        validateRequiredFields(request.getName(), request.getSku(), request.getPrice(), request.getStock());
        validatePriceAndStock(request.getPrice(), request.getStock());
        if (productRepository.existsBySku(request.getSku())) {
            throw new ConflictException("SKU already exists: " + request.getSku());
        }

        Product product = productApplicationMapper.toDomain(request);
        Product savedProduct = productRepository.save(product);
        return productApplicationMapper.toResponse(savedProduct);
    }

    public ProductResponse findById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        return productApplicationMapper.toResponse(product);
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productApplicationMapper::toResponse)
                .toList();
    }

    public ProductResponse update(UUID id, UpdateProductRequest request) {
        Product currentProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        validateRequiredFields(request.getName(), request.getSku(), request.getPrice(), request.getStock());
        validatePriceAndStock(request.getPrice(), request.getStock());

        if (productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new ConflictException("SKU already exists: " + request.getSku());
        }

        productApplicationMapper.applyUpdate(currentProduct, request);
        Product updatedProduct = productRepository.update(currentProduct);
        return productApplicationMapper.toResponse(updatedProduct);
    }

    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private void validateRequiredFields(String name, String sku, BigDecimal price, Integer stock) {
        if (isBlank(name)) {
            throw new BadRequestException("name is required");
        }
        if (isBlank(sku)) {
            throw new BadRequestException("sku is required");
        }
        if (price == null) {
            throw new BadRequestException("price is required");
        }
        if (stock == null) {
            throw new BadRequestException("stock is required");
        }
    }

    private void validatePriceAndStock(BigDecimal price, Integer stock) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("price must be greater than zero");
        }
        if (stock < 0) {
            throw new BadRequestException("stock must be greater than or equal to zero");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
