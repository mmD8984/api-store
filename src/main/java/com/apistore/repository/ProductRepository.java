package com.apistore.repository;

import com.apistore.model.dto.ProductListDTO;
import com.apistore.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT new com.apistore.model.dto.ProductListDTO(
            p.id,
            p.name,
            c.name,
            MIN(v.price) AS price,
            MIN(v.originalPrice) AS originalPrice,
            COALESCE(rs.avgRating, 0) AS avgRating,
            rs.totalReviews,
            p.thumbnail
        )
        FROM Product p
        JOIN p.category c
        JOIN p.variants v
        LEFT JOIN p.ratingSummary rs
        WHERE v.isActive = true
          AND (:category IS NULL OR c.slug = :category)
          AND (:minPrice IS NULL OR v.price >= :minPrice)
          AND (:maxPrice IS NULL OR v.price <= :maxPrice)
        GROUP BY p.id, p.name, c.name, rs.avgRating, rs.totalReviews, p.thumbnail
    """)
    Page<ProductListDTO> findProductsForList(
            @Param("category") String category,
            @Param("minPrice") java.math.BigDecimal minPrice,
            @Param("maxPrice") java.math.BigDecimal maxPrice,
            Pageable pageable
    );
}
