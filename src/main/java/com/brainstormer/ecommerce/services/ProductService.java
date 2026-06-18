package com.brainstormer.ecommerce.services;

import com.brainstormer.ecommerce.dtos.ProductRequestDto;
import com.brainstormer.ecommerce.dtos.ProductResponseDto;
import com.brainstormer.ecommerce.dtos.ProductResponseWithDetailsDto;
import com.brainstormer.ecommerce.exceptions.ResourceNotFoundException;
import com.brainstormer.ecommerce.repositories.ProductRepository;
import com.brainstormer.ecommerce.schema.Category;
import com.brainstormer.ecommerce.schema.Product;
import com.brainstormer.ecommerce.services.cache.ProductRedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductRedisCache productRedisCacheService;

    public List<ProductResponseDto> getAllProducts() {
        var products = productRepository.findAll();
        return products.stream()
                .map(product -> ProductResponseDto.builder()
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
//                        .category(product.getCategory().getName())
                        .rating(product.getRating())
                        .build())
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long id) {
        Optional<ProductResponseDto> cacheProductResponse = productRedisCacheService.getSummary(id);
        if (cacheProductResponse.isPresent()) {
                return cacheProductResponse.get();
        }
        ProductResponseDto response = productRepository.findById(id)
                .map(product -> ProductResponseDto.builder()
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
//                        .category(product.getCategory().getName())
                        .rating(product.getRating())
                        .build())
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ResourceNotFoundException("Product with id " + id + " not found");
                });
        // add to avoid cache miss next time
        productRedisCacheService.putSummary(id, response);
        return response;
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Category category = categoryService.getCategoryEntityById(productRequestDto.getCategoryId());

        Product newProduct = Product.builder()
                .title(productRequestDto.getTitle())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .imageUrl(productRequestDto.getImageUrl())
                .category(category)
                .rating(productRequestDto.getRating())
                .build();

        Product savedProduct = productRepository.save(newProduct);
        return ProductResponseDto.builder()
                .title(savedProduct.getTitle())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .imageUrl(savedProduct.getImageUrl())
//                .category(savedProduct.getCategory().getName())
                .rating(savedProduct.getRating())
                .build();
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            log.error("Product not found with id: {}", id);
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    public List<ProductResponseDto> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category)
                .stream()
                .map(product -> ProductResponseDto.builder()
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
//                        .category(product.getCategory().getName())
                        .rating(product.getRating())
                        .build())
                .collect(Collectors.toList());
    }

    public List<String> getAllUniqueCategory() {
        return productRepository.findDistinctCategory()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    public List<ProductResponseWithDetailsDto> getProductDetailsById(Long id) {
        return productRepository.findProductWithDetailsById(id)
                .stream()
                .map(product -> ProductResponseWithDetailsDto.builder()
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .category(product.getCategory().getName())
                        .rating(product.getRating())
                        .build())
                .collect(Collectors.toList());
    }
}
