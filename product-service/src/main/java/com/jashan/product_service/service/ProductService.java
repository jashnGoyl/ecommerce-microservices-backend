package com.jashan.product_service.service;

import com.jashan.product_service.dto.ProductRequestDTO;
import com.jashan.product_service.dto.ProductResponseDTO;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

}
