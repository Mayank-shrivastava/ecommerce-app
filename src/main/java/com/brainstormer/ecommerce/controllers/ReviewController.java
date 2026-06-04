package com.brainstormer.ecommerce.controllers;

import com.brainstormer.ecommerce.schema.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    @GetMapping
    public List<Review> getAllReviews() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping
    public Review createReview() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/product/{productId}")
    public List<Review> getReviewsByProductId(@PathVariable Long productId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/order/{orderId}")
    public List<Review> getReviewsByOrderId(@PathVariable Long orderId) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
