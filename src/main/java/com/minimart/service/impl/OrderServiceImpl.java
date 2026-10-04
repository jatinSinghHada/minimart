package com.minimart.service.impl;

import com.minimart.dto.*;
import com.minimart.entity.*;
import com.minimart.repository.*;
import com.minimart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.minimart.exception.BadRequestException;
import com.minimart.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {

        // 1. VALIDATE CUSTOMER
        Customer customer = customerRepository.findById(
                requestDTO.getCustomerId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Customer not found"));

        // 2. CREATE ORDER OBJECT
        CustomerOrder order = CustomerOrder.builder()
                .customer(customer)
                .status("CONFIRMED")
                .totalAmount(BigDecimal.ZERO)
                .build();

        CustomerOrder savedOrder =
                customerOrderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();

         // 3. PROCESS EACH ITEM
        for (OrderItemRequestDTO itemDTO :
                requestDTO.getItems()) {

            Product product = productRepository.findById(
                    itemDTO.getProductId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException("Product not found"));
                    

        // 4. STOCK VALIDATION
        if (product.getStockQty() < itemDTO.getQuantity()) {
            throw new BadRequestException("Insufficient stock for product: " + product.getName());
        }

        // 5. DEDUCT STOCK
        product.setStockQty(product.getStockQty() - itemDTO.getQuantity());
        productRepository.save(product);

        // 6. ITEM TOTAL
        BigDecimal itemTotal = product.getPrice().multiply(
                            BigDecimal.valueOf(
                                    itemDTO.getQuantity()
                            )
                    );

        // 7. ADD TO FINAL TOTAL
        totalAmount = totalAmount.add(itemTotal);

        // 8. CREATE ORDER ITEM
            OrderItem orderItem = OrderItem.builder()
                    .customerOrder(savedOrder)
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        // 9. SAVE ORDER ITEMS
        orderItemRepository.saveAll(orderItems);

        // 10. UPDATE FINAL TOTAL
        savedOrder.setTotalAmount(totalAmount);
        customerOrderRepository.save(savedOrder);

        // 11. RETURN RESPONSE
        return mapToOrderResponse(savedOrder, orderItems);
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {

        CustomerOrder order =
                customerOrderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"));

        List<OrderItem> orderItems =
                order.getOrderItems();

        return mapToOrderResponse(order, orderItems);
    }

    @Override
    public OrderResponseDTO cancelOrder(Long orderId) {

        CustomerOrder order =
                customerOrderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"));

    // CHECK ALREADY CANCELLED
        if ("CANCELLED".equals(order.getStatus())) {
            throw new BadRequestException(
                    "Order already cancelled");
        }

        // RESTORE STOCK
        for (OrderItem item : order.getOrderItems()) {

            Product product = item.getProduct();

            product.setStockQty(
                    product.getStockQty()
                            + item.getQuantity()
            );

            productRepository.save(product);
        }

         // UPDATE STATUS
        order.setStatus("CANCELLED");

        customerOrderRepository.save(order);

        return mapToOrderResponse(
                order,
                order.getOrderItems()
        );
    }

        @Override
        public Page<OrderResponseDTO> getAllOrders(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return customerOrderRepository.findAll(pageable)
                .map(order -> mapToOrderResponse(
                        order,
                        order.getOrderItems()
                ));
        }

        // @Override
        // public Page<OrderResponseDTO> getConfirmedOrders(int page, int size) {
        //         Pageable pageable = PageRequest.of(page, size);

        //         return customerOrderRepository
        //                 .findByStatus("CONFIRMED", pageable)
        //                 .map(order -> mapToOrderResponse(
        //                         order,
        //                         order.getOrderItems()
        //                 ));
        

    // ENTITY -> RESPONSE DTO
    private OrderResponseDTO mapToOrderResponse(
            CustomerOrder order,
            List<OrderItem> orderItems) {

        List<OrderItemResponseDTO> itemResponses =
                orderItems.stream()
                        .map(item -> OrderItemResponseDTO.builder()
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .itemTotal(
                                        item.getUnitPrice().multiply(
                                                BigDecimal.valueOf(
                                                        item.getQuantity()
                                                )
                                        )
                                )
                                .build())
                        .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(itemResponses)
                .build();
    }
    
}
