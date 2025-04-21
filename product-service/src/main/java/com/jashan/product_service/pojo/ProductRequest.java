package com.jashan.product_service.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    private String name;

    private String description;

    private double price;

    private String category;

    private String brand;

    private int quantity;
}
