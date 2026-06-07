package com.brainstormer.ecommerce.adapters;

import org.springframework.stereotype.Component;

import com.brainstormer.ecommerce.dtos.GetReviewResponseDto;
import com.brainstormer.ecommerce.schema.Review;


@Component
public class ReviewAdapter {
    public GetReviewResponseDto mapToGetReviewResponseDto(Review review) {
        return GetReviewResponseDto.builder()
            .id(review.getId())
            .productId(review.getProduct().getId())
            .orderId(review.getOrder().getId())
            .rating(review.getRating())
            .comment(review.getComment())
            .createdAt(review.getCreatedAt())
            .build();
    }
}
