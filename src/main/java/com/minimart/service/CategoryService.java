package com.minimart.service;

import com.minimart.dto.CategoryRequestDTO;
import com.minimart.dto.CategoryResponseDTO;
import com.minimart.dto.CategoryUpdateDTO;

import java.util.List;
public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(Long id);

    void deleteCategoryById(Long id);

    CategoryUpdateDTO updateCategory(CategoryUpdateDTO updateDTO);
}
