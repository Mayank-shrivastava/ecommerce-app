package com.brainstormer.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.brainstormer.ecommerce.adapters.ReviewAdapter;
import com.brainstormer.ecommerce.dtos.GetReviewResponseDto;
import com.brainstormer.ecommerce.dtos.ReviewRequestDto;
import com.brainstormer.ecommerce.exceptions.ResourceNotFoundException;
import com.brainstormer.ecommerce.repositories.OrderRepository;
import com.brainstormer.ecommerce.repositories.ProductRepository;
import com.brainstormer.ecommerce.repositories.ReviewRepository;
import com.brainstormer.ecommerce.schema.Order;
import com.brainstormer.ecommerce.schema.Product;
import com.brainstormer.ecommerce.schema.Review;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewAdapter reviewAdapter;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public List<GetReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll()
            .stream()
            .map(reviewAdapter::mapToGetReviewResponseDto)
            .collect(Collectors.toList());
    }

    public GetReviewResponseDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        return reviewAdapter.mapToGetReviewResponseDto(review);
    }

    public List<GetReviewResponseDto> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId)
            .stream()
            .map(reviewAdapter::mapToGetReviewResponseDto)
            .collect(Collectors.toList());
    }

    public List<GetReviewResponseDto> getReviewsByOrderId(Long orderId) {
        return reviewRepository.findByOrderId(orderId)
            .stream()
            .map(reviewAdapter::mapToGetReviewResponseDto)
            .collect(Collectors.toList());
    }

    public GetReviewResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        Product product = productRepository.findById(reviewRequestDto.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + reviewRequestDto.getProductId()));

        Order order = orderRepository.findById(reviewRequestDto.getOrderId())
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + reviewRequestDto.getOrderId()));

        Review review = Review.builder()
            .product(product)
            .order(order)
            .rating(reviewRequestDto.getRating())
            .comment(reviewRequestDto.getComment())
            .build();

        return reviewAdapter.mapToGetReviewResponseDto(reviewRepository.save(review));
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }
}
