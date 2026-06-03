package com.brainstormer.ecommerce.services;

import com.brainstormer.ecommerce.adapters.OrderAdapter;
import com.brainstormer.ecommerce.dtos.OrderResponseDto;
import com.brainstormer.ecommerce.exceptions.ResourceNotFoundException;
import com.brainstormer.ecommerce.repositories.OrderProductRepository;
import com.brainstormer.ecommerce.repositories.OrderRepository;
import com.brainstormer.ecommerce.repositories.ProductRepository;
import com.brainstormer.ecommerce.schema.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderAdapter orderAdapter;

    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderAdapter.mapToOrderResponseDtoList(orders);
    }

    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return orderAdapter.mapToOrderResponseDto(order);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

}
