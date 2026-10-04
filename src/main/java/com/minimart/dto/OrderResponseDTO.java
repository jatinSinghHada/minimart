package com.minimart.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long orderId;

    private Long customerId;

    private String customerName;

    private LocalDateTime orderDate;

    private String status;

    private BigDecimal totalAmount;

    private List<OrderItemResponseDTO> items;
}
