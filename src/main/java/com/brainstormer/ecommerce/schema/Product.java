package com.brainstormer.ecommerce.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id; // primary key
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT") // for longer descriptions
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    private String imageUrl;
    private String category;
    private String rating;
}
