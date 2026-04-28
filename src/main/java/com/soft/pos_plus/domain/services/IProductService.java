package com.soft.pos_plus.domain.services;

import java.util.List;
import java.util.UUID;

import com.soft.pos_plus.application.dtos.CreateProductRequest;
import com.soft.pos_plus.application.dtos.ProductResponse;
import com.soft.pos_plus.application.dtos.UpdateProductRequest;

public interface IProductService {

    ProductResponse create(CreateProductRequest request);

    ProductResponse findById(UUID id);

    List<ProductResponse> findAll();

    ProductResponse update(UUID id, UpdateProductRequest request);

    void delete(UUID id);
}
