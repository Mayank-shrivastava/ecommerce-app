package com.brainstormer.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderItemActionDto {
    private Long productId;
    private Integer quantity;
    private OrderItemAction action;
}
