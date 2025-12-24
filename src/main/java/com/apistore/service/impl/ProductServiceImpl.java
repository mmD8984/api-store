package com.apistore.service.impl;

import com.apistore.model.dto.ProductListDTO;
import com.apistore.repository.ProductRepository;
import com.apistore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<ProductListDTO> getProducts(String category, BigDecimal minPrice, BigDecimal maxPrice, int page, int size, String sort) {

        // 1️⃣ Tạo Sort dynamic
        Sort sortObj;
        switch (sort) {
            case "price-low":
                sortObj = Sort.by("price").ascending();
                break;
            case "price-high":
                sortObj = Sort.by("price").descending();
                break;
            case "rating":
                sortObj = Sort.by("avgRating").descending();
                break;
            case "newest":
                sortObj = Sort.by("id").descending();
                break;
            default:
                sortObj = Sort.by("id").descending();
        }

        // 2️⃣ Tạo Pageable với Sort
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // 3️⃣ Gọi repository, JPQL query trả Page<ProductListDTO>
        return productRepository.findProductsForList(category, minPrice, maxPrice, pageable);
    }
}
