package com.jashan.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDTO {

    private String name;

    private String description;

    private double price;

    private String category;

    private String brand;

    private int quantity;
}
