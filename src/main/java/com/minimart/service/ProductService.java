package com.minimart.service;

import com.minimart.dto.ProductRequestDTO;
import com.minimart.dto.ProductResponseDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponseDTO addProduct(ProductRequestDTO requestDTO);

    Page<ProductResponseDTO> getAllProducts(Pageable pageable, Long categoryId);

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO);

    void deleteProduct(Long id);

    List<ProductResponseDTO> getLowStockProducts(Integer threshold);
}
