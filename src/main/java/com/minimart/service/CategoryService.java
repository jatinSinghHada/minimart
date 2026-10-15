package com.minimart.service;

import com.minimart.dto.CategoryRequestDTO;
import com.minimart.dto.CategoryResponseDTO;
import com.minimart.dto.CategoryUpdateDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    Page<CategoryResponseDTO> getAllCategories(Pageable pageable);

    CategoryResponseDTO getCategoryById(Long id);

    void deleteCategoryById(Long id);

    CategoryUpdateDTO updateCategory(CategoryUpdateDTO updateDTO);
}
