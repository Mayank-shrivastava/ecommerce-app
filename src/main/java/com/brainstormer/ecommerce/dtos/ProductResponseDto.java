package com.brainstormer.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;
    private String rating;
}
