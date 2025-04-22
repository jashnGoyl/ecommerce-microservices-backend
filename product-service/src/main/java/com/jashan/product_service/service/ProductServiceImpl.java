package com.jashan.product_service.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.jashan.product_service.dto.ProductRequestDTO;
import com.jashan.product_service.dto.ProductResponseDTO;
import com.jashan.product_service.entity.Product;
import com.jashan.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        log.info("Creating product: {}", productRequestDTO.getName());

        Product product = modelMapper.map(productRequestDTO, Product.class);
        log.debug("Mapped request to Product entity: {}", product);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        log.info("Persisted the product object", savedProduct);

        ProductResponseDTO productResponseDTO = modelMapper.map(savedProduct, ProductResponseDTO.class);
        log.debug("Mapped saved product entity to product response dto: {}", productResponseDTO);

        return productResponseDTO;
    }

}
