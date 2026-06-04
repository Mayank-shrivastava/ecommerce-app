package com.brainstormer.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderRequestDto {

    private List<OrderItemRequestDto> orderItems;
}
