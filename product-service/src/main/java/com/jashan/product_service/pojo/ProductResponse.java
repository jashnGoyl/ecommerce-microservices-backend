package com.jashan.product_service.pojo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private double price;

    private String category;

    private String brand;

    private int quantity;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("create_at")
    private LocalDateTime updatedAt;
}
