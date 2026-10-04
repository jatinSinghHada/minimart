package com.minimart.service;

import com.minimart.dto.OrderRequestDTO;
import com.minimart.dto.OrderResponseDTO;

import org.springframework.data.domain.Page;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);

    OrderResponseDTO getOrderById(Long orderId);

    OrderResponseDTO cancelOrder(Long orderId);

    Page<OrderResponseDTO> getAllOrders(int page, int size);

    // Page<OrderResponseDTO> getConfirmedOrders(int page, int size);

    // Page<OrderResponseDTO> getCancelledOrders(int page, int size);
}
