package com.brainstormer.ecommerce.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductResponseDto {
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String rating;
}
