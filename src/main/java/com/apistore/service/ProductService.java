package com.apistore.service;

import com.apistore.model.dto.ProductListDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface ProductService {
    Page<ProductListDTO> getProducts(
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sort
    );
}