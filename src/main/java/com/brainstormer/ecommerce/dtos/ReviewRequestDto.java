package com.brainstormer.ecommerce.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequestDto {
    private Long productId;
    private Long orderId;
    private BigDecimal rating;
    private String comment;
}
