package com.soft.pos_plus.api.controllers;

import com.soft.pos_plus.application.dtos.CreateProductRequest;
import com.soft.pos_plus.application.dtos.ProductResponse;
import com.soft.pos_plus.application.dtos.UpdateProductRequest;
import com.soft.pos_plus.application.services.ProductService;

import lombok.RequiredArgsConstructor;

import com.soft.pos_plus.api.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@RequestBody CreateProductRequest request) {
        ProductResponse data = productService.create(request);
        ApiResponse<ProductResponse> response = ApiResponse.success("Product created successfully", data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> findById(@PathVariable UUID id) {
        ProductResponse data = productService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Product found successfully", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAll() {
        List<ProductResponse> data = productService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable UUID id, @RequestBody UpdateProductRequest request) {
        ProductResponse data = productService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}
