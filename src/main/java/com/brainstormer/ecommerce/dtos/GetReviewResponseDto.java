package com.brainstormer.ecommerce.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class GetReviewResponseDto {
    private Long id;
    private Long productId;
    private Long orderId;
    private BigDecimal rating;
    private String comment;
    private LocalDateTime createdAt;
}
