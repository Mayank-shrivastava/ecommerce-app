package com.brainstormer.ecommerce.controllers;


import com.brainstormer.ecommerce.dtos.CategoryResponseDto;
import com.brainstormer.ecommerce.dtos.CreateCategoryRequestDto;
import com.brainstormer.ecommerce.services.CategoryService;
import com.brainstormer.ecommerce.utlis.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(@RequestBody CreateCategoryRequestDto requestDto) {
        var category = categoryService.createCategory(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(category, "Category created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        var categories = categoryService.getAllCategories();
        return ResponseEntity
                .ok(ApiResponse.success(categories, "Categories fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById(@PathVariable Long id) {
        var category = categoryService.getCategoryById(id);
        return ResponseEntity
                .ok(ApiResponse.success(category, "Category fetched successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity
                .ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}
