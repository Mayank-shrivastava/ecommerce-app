package com.brainstormer.ecommerce.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainstormer.ecommerce.dtos.GetReviewResponseDto;
import com.brainstormer.ecommerce.dtos.ReviewRequestDto;
import com.brainstormer.ecommerce.services.ReviewService;
import com.brainstormer.ecommerce.utlis.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetReviewResponseDto>>> getAllReviews() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(reviewService.getAllReviews(), "Reviews fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GetReviewResponseDto>> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(reviewService.createReview(reviewRequestDto), "Review created successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(null, "Review deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetReviewResponseDto>> getReviewById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(reviewService.getReviewById(id), "Review fetched successfully"));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<GetReviewResponseDto>>> getReviewsByProductId(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(reviewService.getReviewsByProductId(productId), "Reviews fetched successfully"));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<GetReviewResponseDto>>> getReviewsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(reviewService.getReviewsByOrderId(orderId), "Reviews fetched successfully"));
    }
}
