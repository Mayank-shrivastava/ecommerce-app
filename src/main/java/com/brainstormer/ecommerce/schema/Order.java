package com.brainstormer.ecommerce.schema;

import com.brainstormer.ecommerce.schema.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private OrderStatus orderStatus;

    // First approach for Many-to-many relationship
    // Drawbacks: Can only add two columns for referencing foreign key constraints
//    @ManyToMany
//    @JoinTable(
//            name = "order_product_mapping",
//            joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_id"))
//    private List<Product> products;



}
