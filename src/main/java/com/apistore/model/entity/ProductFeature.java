package com.apistore.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nhiều feature thuộc về 1 product
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Nội dung điểm nổi bật
     * VD: "Chip Apple M3 Max hiệu năng vượt trội"
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String feature;

    /**
     * Thứ tự hiển thị
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
