package com.brainstormer.ecommerce.repositories;

import com.brainstormer.ecommerce.schema.OrderProductMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProductMapping, Long> {
    List<OrderProductMapping> findByOrderId(Long orderId);
}
