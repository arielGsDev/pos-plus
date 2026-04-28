package com.soft.pos_plus.infrastructure.persistence.sqlserver;

import com.soft.pos_plus.domain.entities.Product;
import com.soft.pos_plus.domain.repositories.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class SqlServerProductRepository implements ProductRepository {

    private final SpringDataSqlServerProductJpaRepository jpaRepository;
    private final SqlServerProductMapper mapper;

    public SqlServerProductRepository(
            SpringDataSqlServerProductJpaRepository jpaRepository,
            SqlServerProductMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        SqlServerProductEntity entity = mapper.toEntity(product);
        SqlServerProductEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Product update(Product product) {
        SqlServerProductEntity entity = mapper.toEntity(product);
        SqlServerProductEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsBySku(String sku) {
        return jpaRepository.existsBySku(sku);
    }

    @Override
    public boolean existsBySkuAndIdNot(String sku, UUID id) {
        return jpaRepository.existsBySkuAndIdNot(sku, id);
    }
}
