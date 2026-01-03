package com.apistore.controller;

import com.apistore.model.dto.ProductDetailDTO;
import com.apistore.model.dto.RelatedProductDTO;
import com.apistore.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/public/products")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    /**
     * API public - chi tiết sản phẩm
     * GET /public/products/{id}
     */
    @GetMapping("/{id}")
    public ProductDetailDTO getProductDetail(@PathVariable Long id) {
        return productDetailService.getProductDetail(id);
    }

    /**
     * API public - sản phẩm liên quan
     * GET /public/products/{id}/related?limit=4
     */
    @GetMapping("/{id}/related")
    public List<RelatedProductDTO> getRelatedProducts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "4") Integer limit
    ) {
        return productDetailService.getRelatedProducts(id, limit);
    }
}
