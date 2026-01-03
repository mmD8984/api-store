package com.apistore.model.dto;

import java.math.BigDecimal;

public record RelatedProductDTO(
        Integer id,
        String name,
        String thumbnail,
        BigDecimal price
) {}
