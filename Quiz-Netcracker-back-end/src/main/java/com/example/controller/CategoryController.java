package com.example.controller;

import com.example.dto.CategoryDto;
import com.example.model.Category;
import com.example.service.interfaces.CategoryService;
import com.example.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final Mapper mapper;

    @Autowired
    public CategoryController(CategoryService categoryService, Mapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return categoryService.findAllCategory()
                .stream()
                .map(mapper::toShortCategoryDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getQuestions(@PathVariable UUID categoryId) {
        return mapper.toCategoryDto(categoryService.findCategoryById(categoryId));
    }

    @PostMapping("/category")
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        Category category = mapper.fromCategoryDro(categoryDto);
        return mapper.toCategoryDto(categoryService.saveCategory(category));
    }

    @PutMapping("/category/{categoryId}")
    public CategoryDto updateCategory(@PathVariable UUID categoryId,
                                   @Valid @RequestBody CategoryDto categoryDto) {
        Category category = mapper.fromCategoryDro(categoryDto);
        return mapper.toCategoryDto(categoryService.updateCategory(categoryId, category));
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }
}