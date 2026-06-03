package com.brainstormer.ecommerce.adapters;

import com.brainstormer.ecommerce.dtos.OrderItemResponseDto;
import com.brainstormer.ecommerce.dtos.OrderResponseDto;
import com.brainstormer.ecommerce.repositories.OrderProductRepository;
import com.brainstormer.ecommerce.schema.Order;
import com.brainstormer.ecommerce.schema.OrderProductMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderAdapter {

    private final OrderProductRepository orderProductRepository;

    public List<OrderResponseDto> mapToOrderResponseDtoList(List<Order> orders) {
        return orders.stream().map(this::mapToOrderResponseDto).toList();
    }

    public OrderResponseDto mapToOrderResponseDto(Order order) {
        List<OrderProductMapping> orderProducts = orderProductRepository.findByOrderId(order.getId());
        return OrderResponseDto.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(mapToOrderItemResponseDto(orderProducts))
                .build();
    }

    public List<OrderItemResponseDto> mapToOrderItemResponseDto(List<OrderProductMapping> orderProducts) {
        return orderProducts.stream().map(orderProduct -> OrderItemResponseDto.builder()
                .productId(orderProduct.getProduct().getId())
                .productName(orderProduct.getProduct().getTitle())
                .productPrice(orderProduct.getProduct().getPrice())
                .productImage(orderProduct.getProduct().getImageUrl())
                .subTotal(orderProduct.getProduct()
                            .getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity())))
                .quantity(orderProduct.getQuantity())
                .build()).toList();
    }
}
