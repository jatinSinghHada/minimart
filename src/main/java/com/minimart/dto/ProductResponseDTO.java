package com.minimart.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    
    private Long id;

    private String name;

    private String sku;

    private BigDecimal price;

    private Integer stockQty;

    private Long categoryId;

    private String categoryName;
}
