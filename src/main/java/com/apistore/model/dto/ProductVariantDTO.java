package com.apistore.model.dto;

import java.math.BigDecimal;

public record ProductVariantDTO(
        Integer id,
        String variantName,
        String color,
        String ram,
        String storage,
        BigDecimal price,
        BigDecimal originalPrice,
        Integer stock,
        String status
) {}
