package com.apistore.model.dto;

import java.math.BigDecimal;

public record ProductListDTO(
        Integer id,
        String name,
        String category,
        BigDecimal price,
        BigDecimal originalPrice,
        BigDecimal avgRating,
        Integer totalReviews,
        String thumbnail
) {}

