package com.brainstormer.ecommerce.controllers;


import com.brainstormer.ecommerce.dtos.CategoryResponseDto;
import com.brainstormer.ecommerce.dtos.CreateCategoryRequestDto;
import com.brainstormer.ecommerce.services.CategoryService;
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
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CreateCategoryRequestDto requestDto) {

        //return new ResponseEntity<>(categoryService.createCategory(requestDto), HttpStatus.CREATED);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(requestDto));
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
