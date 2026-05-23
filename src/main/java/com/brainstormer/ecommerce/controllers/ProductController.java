package com.brainstormer.ecommerce.controllers;

import com.brainstormer.ecommerce.dtos.ProductRequestDto;
import com.brainstormer.ecommerce.dtos.ProductResponseDto;
import com.brainstormer.ecommerce.schema.Product;
import com.brainstormer.ecommerce.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/search")
    public List<ProductResponseDto> getProductByCategory(@RequestParam("categoryName") String category) {
        return productService.getProductByCategory(category);
    }

    @GetMapping("/uniqueCategory")
    public List<String> getAllUniqueCategory() {
        return productService.getAllUniqueCategory();
    }
}
