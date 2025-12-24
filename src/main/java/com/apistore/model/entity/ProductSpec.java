package com.apistore.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_specs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(length = 255, nullable = false)
    private String label;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String value;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
