package com.brainstormer.ecommerce.services;

import com.brainstormer.ecommerce.adapters.OrderAdapter;
import com.brainstormer.ecommerce.dtos.*;
import com.brainstormer.ecommerce.exceptions.ResourceNotFoundException;
import com.brainstormer.ecommerce.repositories.OrderProductRepository;
import com.brainstormer.ecommerce.repositories.OrderRepository;
import com.brainstormer.ecommerce.repositories.ProductRepository;
import com.brainstormer.ecommerce.schema.Order;
import com.brainstormer.ecommerce.schema.OrderProductMapping;
import com.brainstormer.ecommerce.schema.Product;
import com.brainstormer.ecommerce.schema.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        // 1. we need to create a new order instance
        // 2. If the payload (dto) has some order products, add those in the order as well otherwise skip it
        // 3. return the order

        Order order = Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);

        if (orderRequestDto.getOrderItems() != null) {
            List<Long> productIds = orderRequestDto.getOrderItems().stream()
                    .map(item -> item.getProductId())
                    .toList();

            List<Product> products = productRepository.findAllById(productIds);
            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, Function.identity()));

            for (Long id : productIds) {
                if (!productMap.containsKey(id)) {
                    throw new ResourceNotFoundException("Product not found with id: " + id);
                }
            }

            List<OrderProductMapping> orderProducts = new ArrayList<>();
            for (var itemDto : orderRequestDto.getOrderItems()) {
                Product product = productMap.get(itemDto.getProductId());

                OrderProductMapping orderProductMapping = OrderProductMapping.builder()
                        .order(order)
                        .product(product)
                        .quantity(itemDto.getQuantity() != null ? itemDto.getQuantity() : 1)
                        .build();

                orderProducts.add(orderProductMapping);
            }

            orderProductRepository.saveAll(orderProducts);
        }

        return orderAdapter.mapToOrderResponseDto(order);
    }

    public OrderResponseDto updateOrder(Long orderId, UpdateOrderRequestDto updateOrderRequestDto) {
        // Fetch old order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // update the order status if not null
        if (updateOrderRequestDto.getStatus() != null) {
            order.setOrderStatus(updateOrderRequestDto.getStatus());
            orderRepository.save(order);
        }

        // update the order items
        if (updateOrderRequestDto.getOrderItems() != null) {
            // update this without n+1 query problem
            List<Long> productIds = updateOrderRequestDto.getOrderItems().stream()
                    .map(item -> item.getProductId())
                    .toList();

            List<Product> products = productRepository.findAllById(productIds);

            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, Function.identity()));

            for (var pdId : productIds) {
                if (!productMap.containsKey(pdId)) {
                    throw new ResourceNotFoundException("Product not found with id: " + pdId);
                }
            }

            List<OrderProductMapping> toSave = new ArrayList<>();
            List<OrderProductMapping> toDelete = new ArrayList<>();
            Map<Long, OrderProductMapping> existingItems = orderProductRepository.findByOrderWithProduct(order)
                    .stream().collect(Collectors.toMap(op -> op.getProduct().getId(), Function.identity()));

            for (OrderItemActionDto itemAction : updateOrderRequestDto.getOrderItems()) {
                Product product = productMap.get(itemAction.getProductId());
                OrderProductMapping existing = existingItems.get(product.getId());

                switch (itemAction.getAction()) {
                    case OrderItemAction.ADD -> {
                        if (existing != null) {
                            int addQty = itemAction.getQuantity() != null ? itemAction.getQuantity() : 1;
                            existing.setQuantity(existing.getQuantity() + addQty);
                            toSave.add(existing);
                        } else {
                            OrderProductMapping newItem = OrderProductMapping.builder()
                                    .order(order)
                                    .product(product)
                                    .quantity(itemAction.getQuantity() != null ? itemAction.getQuantity() : 1)
                                    .build();

                            existingItems.put(product.getId(), newItem);
                            toSave.add(newItem);
                        }
                    }

                    case OrderItemAction.REMOVE -> {
                        if (existing == null) {
                            throw new ResourceNotFoundException("Product not found with id: "+ product.getId());
                        }
                        toDelete.add(existing);
                        existingItems.remove(product.getId());
                    }
                    case OrderItemAction.INCREMENT -> {
                        if (existing == null) {
                            throw new ResourceNotFoundException("Product not found with id: "+ product.getId());
                        }
                        existing.setQuantity(existing.getQuantity() + 1);
                        toSave.add(existing);
                    }
                    case OrderItemAction.DECREMENT -> {
                        if (existing == null) {
                            throw new ResourceNotFoundException("Product not found with id: "+ product.getId());
                        }

                        if (existing.getQuantity() <= 1) {
                            toDelete.add(existing);
                            existingItems.remove(product.getId());
                        } else {
                            existing.setQuantity(existing.getQuantity() - 1);
                            toSave.add(existing);
                        }
                    }
                }
            }

            if (!toSave.isEmpty()) {
                orderProductRepository.saveAll(toSave);
            }
            if (!toDelete.isEmpty()) {
                orderProductRepository.deleteAll(toDelete);
            }
        }

        return orderAdapter.mapToOrderResponseDto(order);
    }

    public OrderSummaryResponseDto getOrderSummary(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + id));

        List<OrderProductMapping> orderProducts = orderProductRepository.findByOrderWithProduct(order);

        List<OrderItemResponseDto> items = orderAdapter.mapToOrderItemResponseDto(orderProducts);

        int totalItems = orderProducts.stream().mapToInt(OrderProductMapping::getQuantity).sum();

        BigDecimal totalPrice = orderProducts.stream().map(op -> op.getProduct().getPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderSummaryResponseDto.builder()
                    .id(order.getId())
                    .status(order.getOrderStatus())
                    .items(items)
                    .totalPrice(totalPrice)
                    .totalItems(totalItems)
                    .createdAt(order.getCreatedAt())
                    .updatedAt(order.getUpdatedAt())
                    .build();

    }
}

// User -> Cart -> Adds an item -> New Order (Pending)]

// User -> adds more items in the cart -> Same order will be updated

// During checkout -> Order pending -> Success/Failure