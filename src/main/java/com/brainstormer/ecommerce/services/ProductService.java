package com.brainstormer.ecommerce.services;

import com.brainstormer.ecommerce.dtos.ProductRequestDto;
import com.brainstormer.ecommerce.dtos.ProductResponseDto;
import com.brainstormer.ecommerce.repositories.ProductRepository;
import com.brainstormer.ecommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponseDto> getAllProducts() {
        var products = productRepository.findAll();
        return products.stream()
                .map(product -> ProductResponseDto.builder()
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .category(product.getCategory())
                        .rating(product.getRating())
                        .build())
                .toList();
    }

    public ProductResponseDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> ProductResponseDto.builder()
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .category(product.getCategory())
                        .rating(product.getRating())
                        .build())
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product newProduct = Product.builder()
                .title(productRequestDto.getTitle())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .imageUrl(productRequestDto.getImageUrl())
                .category(productRequestDto.getCategory())
                .rating(productRequestDto.getRating())
                .build();

        Product savedProduct = productRepository.save(newProduct);
        return ProductResponseDto.builder()
                .title(savedProduct.getTitle())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .imageUrl(savedProduct.getImageUrl())
                .category(savedProduct.getCategory())
                .rating(savedProduct.getRating())
                .build();
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductResponseDto> getProductByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(product -> ProductResponseDto.builder()
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .category(product.getCategory())
                        .rating(product.getRating())
                        .build())
                .toList();
    }

    public List<String> getAllUniqueCategory() {
        return productRepository.findDistinctCategory();
    }
}
