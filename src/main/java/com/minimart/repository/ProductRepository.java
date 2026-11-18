package com.minimart.repository;

import com.minimart.dto.ProductResponseDTO;
import com.minimart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByStockQtyLessThan(Integer stockQty);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    boolean existsByCategoryId(Long id);

    @Query("""
        SELECT new com.minimart.dto.ProductResponseDTO(
            p.id,
            p.name,
            p.sku,
            p.price,
            p.stockQty,
            c.id,
            c.name
        )
        FROM Product p
        JOIN p.category c
        WHERE (:categoryId IS NULL OR c.id = :categoryId)
        """)
    Page<ProductResponseDTO> findAllProductDTO(Pageable pageable, Long categoryId);

    boolean existsBySku(String sku);
}
