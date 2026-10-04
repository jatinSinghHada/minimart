package com.minimart.service;

import com.minimart.dto.ProductRequestDTO;
import com.minimart.dto.ProductResponseDTO;

import java.util.List;

import org.springframework.data.domain.Page;

public interface ProductService {

    ProductResponseDTO addProduct(ProductRequestDTO requestDTO);

    Page<ProductResponseDTO> getAllProducts(int page, int size, Long categoryId);

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO);

    void deleteProduct(Long id);

    List<ProductResponseDTO> getProductsByCategory(Long categoryId);

    List<ProductResponseDTO> getLowStockProducts(Integer threshold);
}
