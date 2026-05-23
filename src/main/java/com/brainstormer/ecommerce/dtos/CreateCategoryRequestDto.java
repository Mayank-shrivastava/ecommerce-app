package com.brainstormer.ecommerce.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryRequestDto {
    private String name;
}
