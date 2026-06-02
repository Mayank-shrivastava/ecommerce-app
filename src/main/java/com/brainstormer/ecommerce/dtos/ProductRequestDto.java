package com.brainstormer.ecommerce.dtos;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDto {
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Long categoryId;
    private BigDecimal rating;
}
