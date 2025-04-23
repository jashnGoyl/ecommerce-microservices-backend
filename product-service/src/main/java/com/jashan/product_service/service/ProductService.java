package com.jashan.product_service.service;

import java.util.List;

import com.jashan.product_service.dto.ProductRequestDTO;
import com.jashan.product_service.dto.ProductResponseDTO;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO getProductById(Long productId);

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO);

    void deleteProduct(Long productId);
}
