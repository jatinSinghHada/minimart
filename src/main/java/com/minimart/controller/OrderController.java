package com.minimart.controller;

import com.minimart.dto.OrderRequestDTO;
import com.minimart.dto.OrderResponseDTO;
import com.minimart.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponseDTO createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {

        return orderService.createOrder(requestDTO);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDTO getOrderById(@PathVariable Long orderId) {

        return orderService.getOrderById(orderId);
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderResponseDTO cancelOrder(@PathVariable Long orderId) {

        return orderService.cancelOrder(orderId);
    }

    @GetMapping
    public Page<OrderResponseDTO> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return orderService.getAllOrders(page, size);
    }

   
}
