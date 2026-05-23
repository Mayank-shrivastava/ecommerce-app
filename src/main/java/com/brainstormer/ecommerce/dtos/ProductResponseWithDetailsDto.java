package com.brainstormer.ecommerce.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductResponseWithDetailsDto extends ProductResponseDto {
    private String category;
}
