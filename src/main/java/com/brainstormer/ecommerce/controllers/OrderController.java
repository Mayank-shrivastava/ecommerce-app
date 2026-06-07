package com.brainstormer.ecommerce.controllers;
import com.brainstormer.ecommerce.dtos.OrderRequestDto;
import com.brainstormer.ecommerce.dtos.OrderResponseDto;
import com.brainstormer.ecommerce.dtos.OrderSummaryResponseDto;
import com.brainstormer.ecommerce.dtos.UpdateOrderRequestDto;
import com.brainstormer.ecommerce.services.OrderService;
import com.brainstormer.ecommerce.utlis.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Get All Orders
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(orders, "Orders fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(orderResponseDto, "Order Created Successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(null, "Order deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(@PathVariable Long id) {
        OrderResponseDto orderResponseDto = orderService.getOrderById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(orderResponseDto, "Order fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequestDto updateOrderRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(orderService.updateOrder(id, updateOrderRequestDto), "Order updated successfully"));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<ApiResponse<OrderSummaryResponseDto>> getOrderSummary(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(orderService.getOrderSummary(id), "Order summary fetched successfully"));
    }
}
