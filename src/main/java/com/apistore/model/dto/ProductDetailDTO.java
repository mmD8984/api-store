package com.apistore.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailDTO(
        Integer id,
        String name,
        String description,
        String thumbnail,
        CategoryDTO category,

        BigDecimal avgRating,
        Integer totalReviews,

        List<String> images,
        List<String> features,
        List<ProductSpecDTO> specs,
        List<ProductVariantDTO> variants
) {}
