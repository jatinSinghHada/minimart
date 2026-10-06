package com.minimart.service.impl;

import com.minimart.dto.*;
import com.minimart.entity.Category;
import com.minimart.repository.CategoryRepository;
import com.minimart.repository.ProductRepository;
import com.minimart.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import com.minimart.exception.ResourceNotFoundException;
import com.minimart.exception.ResourceInUseException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {

        Category category = Category.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .build();

        Category saved = categoryRepository.save(category);

        return mapToDTO(saved);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        return mapToDTO(category);
    }

    private CategoryResponseDTO mapToDTO(Category category) {

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public void deleteCategoryById(Long id) {
        Optional<Category> foundCategory = categoryRepository.findById(id);
        if(foundCategory.isEmpty()) {
            throw new ResourceNotFoundException("Category with id " + id + " is not present in DB");
        }

        boolean associatedProducts = productRepository.existsByCategoryId(id);
        if(associatedProducts) {
            throw new ResourceInUseException("Category with id : " + id + " has associated products");
        }

        categoryRepository.deleteById(id);
    }

    private CategoryUpdateDTO mapToUpdateDTO(Category category) {
        return CategoryUpdateDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public CategoryUpdateDTO updateCategory(CategoryUpdateDTO updateDTO) {
        Optional<Category> foundId = categoryRepository.findById(updateDTO.getId());
        if(foundId.isEmpty()) {
            throw new ResourceNotFoundException("Category with provided Id : " + updateDTO.getId() + " is not found");
        }

        Category updateCategoryObj = foundId.get();

        if(updateDTO.getName() != null) {
            updateCategoryObj.setName(updateDTO.getName());
        }

        if(updateDTO.getDescription() != null){
            updateCategoryObj.setDescription(updateDTO.getDescription());
        }

        Category updatedCategoryObj = categoryRepository.save(updateCategoryObj);
        return mapToUpdateDTO(updatedCategoryObj);
    }
}