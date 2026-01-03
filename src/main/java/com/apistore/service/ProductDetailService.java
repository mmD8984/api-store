package com.apistore.service;

import com.apistore.model.dto.*;
import com.apistore.model.entity.Product;
import com.apistore.model.entity.ProductFeature;
import com.apistore.model.entity.ProductImage;
import com.apistore.model.entity.ProductVariant;
import com.apistore.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductRepository productRepository;

    /**
     * Lấy chi tiết sản phẩm (API public)
     */
    @Transactional(readOnly = true)
    public ProductDetailDTO getProductDetail(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return new ProductDetailDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getThumbnail(),

                // Category
                new CategoryDTO(
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getCategory().getSlug()
                ),

                // Rating summary
                product.getRatingSummary() != null
                        ? product.getRatingSummary().getAvgRating()
                        : BigDecimal.ZERO,
                product.getRatingSummary() != null
                        ? product.getRatingSummary().getTotalReviews()
                        : 0,

                // Images
                product.getImages().stream()
                        .map(ProductImage::getImageUrl)
                        .toList(),

                // Features
                product.getFeatures().stream()
                        .map(ProductFeature::getFeature)
                        .toList(),

                // Specs
                product.getSpecs().stream()
                        .map(s -> new ProductSpecDTO(
                                s.getLabel(),
                                s.getValue()
                        ))
                        .toList(),

                // Variants
                product.getVariants().stream()
                        .filter(ProductVariant::getIsActive)
                        .map(v -> new ProductVariantDTO(
                                v.getId(),
                                v.getVariantName(),
                                v.getColor(),
                                v.getRam(),
                                v.getStorage(),
                                v.getPrice(),
                                v.getOriginalPrice(),
                                v.getInventory() != null
                                        ? v.getInventory().getStock()
                                        : 0,
                                v.getInventory() != null
                                        ? v.getInventory().getStatus()
                                        : "out_of_stock"
                        ))
                        .toList()
        );
    }

    /**
     * Lấy danh sách sản phẩm liên quan (cùng category)
     */
    @Transactional(readOnly = true)
    public List<RelatedProductDTO> getRelatedProducts(Long productId, Integer limit) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String categorySlug = product.getCategory().getSlug();

        return productRepository.findRelatedProducts(
                categorySlug,
                productId,
                PageRequest.of(0, limit) // lấy 4 sản phẩm liên quan
        );
    }
}