package com.brainstormer.ecommerce.controllers;


import com.brainstormer.ecommerce.dtos.CategoryResponseDto;
import com.brainstormer.ecommerce.dtos.CreateCategoryRequestDto;
import com.brainstormer.ecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponseDto createCategory(@RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.createCategory(requestDto);
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
