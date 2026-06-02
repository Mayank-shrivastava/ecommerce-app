package com.brainstormer.ecommerce.services;

import com.brainstormer.ecommerce.dtos.CategoryResponseDto;
import com.brainstormer.ecommerce.dtos.ProductRequestDto;
import com.brainstormer.ecommerce.dtos.ProductResponseDto;
import com.brainstormer.ecommerce.dtos.ProductResponseWithDetailsDto;
import com.brainstormer.ecommerce.exceptions.ResourceNotFoundException;
import com.brainstormer.ecommerce.repositories.ProductRepository;
import com.brainstormer.ecommerce.schema.Category;
import com.brainstormer.ecommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final CategoryService categoryService;

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
        return productRepository.findById(id)
                .map(product -> ProductResponseDto.builder()
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
//                        .category(product.getCategory().getName())
                        .rating(product.getRating())
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryById(productRequestDto.getCategoryId());
        Category category = Category.builder()
                .name(categoryResponseDto.getName())
                .build();

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
