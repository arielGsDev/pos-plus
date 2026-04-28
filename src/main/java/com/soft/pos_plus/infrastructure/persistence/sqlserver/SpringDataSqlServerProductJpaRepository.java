package com.soft.pos_plus.infrastructure.persistence.sqlserver;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSqlServerProductJpaRepository extends JpaRepository<SqlServerProductEntity, UUID> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, UUID id);
}
