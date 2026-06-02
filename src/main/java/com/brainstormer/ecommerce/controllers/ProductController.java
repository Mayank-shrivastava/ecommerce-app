package com.brainstormer.ecommerce.controllers;

import com.brainstormer.ecommerce.dtos.ProductRequestDto;
import com.brainstormer.ecommerce.dtos.ProductResponseDto;
import com.brainstormer.ecommerce.dtos.ProductResponseWithDetailsDto;
import com.brainstormer.ecommerce.services.ProductService;
import com.brainstormer.ecommerce.utlis.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts() {
        var products = productService.getAllProducts();
        return ResponseEntity
                .ok(ApiResponse.success(products, "Products fetched successfully"));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Long id) {
        var product = productService.getProductById(id);
        return ResponseEntity
                .ok(ApiResponse.success(product, "Product fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        var product = productService.createProduct(productRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(product, "Product created successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .ok(ApiResponse.success(null, "Product deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductByCategory(@RequestParam("categoryName") String category) {
        var products = productService.getProductByCategory(category);
        return ResponseEntity
                .ok(ApiResponse.success(products, "Products fetched successfully"));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getAllUniqueCategory() {
        var categories = productService.getAllUniqueCategory();
        return ResponseEntity
                .ok(ApiResponse.success(categories, "Categories fetched successfully"));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<List<ProductResponseWithDetailsDto>>> getProductDetails(@PathVariable Long id) {
        var productDetails = productService.getProductDetailsById(id);
        return ResponseEntity
                .ok(ApiResponse.success(productDetails, "Product details fetched successfully"));
    }
}
