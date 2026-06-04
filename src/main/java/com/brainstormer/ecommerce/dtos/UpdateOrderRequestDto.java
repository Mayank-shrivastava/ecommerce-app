package com.brainstormer.ecommerce.dtos;

import com.brainstormer.ecommerce.schema.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UpdateOrderRequestDto {
    private OrderStatus status;
    private List<OrderItemActionDto> orderItems;
}
