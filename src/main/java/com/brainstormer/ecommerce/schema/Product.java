package com.brainstormer.ecommerce.schema;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Product extends BaseEntity {
    private Long id; // primary key
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT") // for longer descriptions
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    private String imageUrl;
    private String rating;

    @ManyToOne(fetch = FetchType.LAZY) // many products can belong to one category
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
