package com.minimart.service.impl;

import com.minimart.dto.ProductRequestDTO;
import com.minimart.dto.ProductResponseDTO;
import com.minimart.entity.Category;
import com.minimart.entity.Product;
import com.minimart.repository.CategoryRepository;
import com.minimart.repository.ProductRepository;
import com.minimart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.minimart.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO requestDTO) {

        Category category = categoryRepository.findById(
                requestDTO.getCategoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));

        Product product = Product.builder()
                .name(requestDTO.getName())
                .sku(requestDTO.getSku())
                .price(requestDTO.getPrice())
                .stockQty(requestDTO.getStockQty())
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);

        return mapToResponseDTO(savedProduct);
    }

    @Override
    public Page<ProductResponseDTO> getAllProducts(int page, int size, Long categoryId) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage;

        if (categoryId != null) {

                productPage = productRepository
                        .findByCategoryId(categoryId, pageable);

        } else {

                productPage = productRepository
                        .findAll(pageable);
        }

        return productPage.map(this::mapToResponseDTO);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return mapToResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(
                requestDTO.getCategoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));

        product.setName(requestDTO.getName());
        product.setSku(requestDTO.getSku());
        product.setPrice(requestDTO.getPrice());
        product.setStockQty(requestDTO.getStockQty());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return mapToResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {

        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getLowStockProducts(Integer threshold) {

        return productRepository.findByStockQtyLessThan(threshold)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ENTITY -> RESPONSE DTO
    private ProductResponseDTO mapToResponseDTO(Product product) {

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .price(product.getPrice())
                .stockQty(product.getStockQty())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .build();
    }
}
