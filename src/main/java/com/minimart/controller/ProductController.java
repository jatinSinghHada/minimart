package com.minimart.controller;

import com.minimart.dto.ProductRequestDTO;
import com.minimart.dto.ProductResponseDTO;
import com.minimart.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponseDTO addProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {

        return productService.addProduct(requestDTO);
    }

    @GetMapping
    public Page<ProductResponseDTO> getAllProducts(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(required = false) Long categoryId) {

        return productService.getAllProducts(page , size, categoryId);
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {

        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO requestDTO) {

        return productService.updateProduct(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponseDTO> getProductsByCategory(@PathVariable Long categoryId) {

        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/low-stock")
    public List<ProductResponseDTO> getLowStockProducts(@RequestParam Integer threshold) {

        return productService.getLowStockProducts(threshold);
    }
}
