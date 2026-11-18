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

import com.minimart.exception.DuplicateResourceException;
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

        Boolean findSku = productRepository.existsBySku(requestDTO.getSku());
        if(findSku) {
                throw new DuplicateResourceException("Product with SKU '" + requestDTO.getSku() + "' already exists");
        }

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
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable, Long categoryId) {

        return productRepository.findAllProductDTO(pageable, categoryId);
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
