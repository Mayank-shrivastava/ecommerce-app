package com.brainstormer.ecommerce.services;

import com.brainstormer.ecommerce.dtos.CategoryResponseDto;
import com.brainstormer.ecommerce.dtos.CreateCategoryRequestDto;
import com.brainstormer.ecommerce.exceptions.ResourceNotFoundException;
import com.brainstormer.ecommerce.repositories.CategoryRepository;
import com.brainstormer.ecommerce.schema.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponseDto createCategory(CreateCategoryRequestDto requestDto) {
        Category newCategory = Category.builder()
                .name(requestDto.getName())
                .build();

        Category category = categoryRepository.save(newCategory);
        return CategoryResponseDto.builder()
                .name(category.getName())
                .build();
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream().map(category -> CategoryResponseDto.builder()
                        .name(category.getName())
                        .build())
                .toList();
    }

    public CategoryResponseDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> CategoryResponseDto.builder()
                        .name(category.getName())
                        .build())
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category with id " + id + " not found");
                });
    }

    public Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category with id " + id + " not found");
                });
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            log.error("Category not found with id: {}", id);
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }
}
