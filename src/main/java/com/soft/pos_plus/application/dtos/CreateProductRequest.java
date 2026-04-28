package com.soft.pos_plus.application.dtos;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {

    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private boolean active;

}
