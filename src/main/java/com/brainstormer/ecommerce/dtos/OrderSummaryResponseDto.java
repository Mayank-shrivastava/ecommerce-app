package com.brainstormer.ecommerce.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.brainstormer.ecommerce.schema.enums.OrderStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSummaryResponseDto {
    private Long id;
    private OrderStatus status;
    private List<OrderItemResponseDto> items;
    private int totalItems;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; 
}
