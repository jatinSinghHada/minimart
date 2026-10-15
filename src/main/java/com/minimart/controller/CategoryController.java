package com.minimart.controller;

import com.minimart.dto.*;
import com.minimart.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;


import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponseDTO createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {

        return categoryService.createCategory(requestDTO);
    }

    @GetMapping
    public Page<CategoryResponseDTO> getAllCategories(@PageableDefault(size=2, page=0, sort="name", direction=Sort.Direction.ASC)
        Pageable pageable) {

        return categoryService.getAllCategories(pageable);
    }


    @GetMapping("/{id}")
    public CategoryResponseDTO getById(@PathVariable Long id) {

        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }

    @PatchMapping("/update")
    public CategoryUpdateDTO updateCategory(@Valid @RequestBody CategoryUpdateDTO updateDTO) {
        return categoryService.updateCategory(updateDTO);
    }
}
