package com.brainstormer.ecommerce.schema;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    private String name;
}
