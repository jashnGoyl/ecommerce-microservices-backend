package com.jashan.product_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.jashan.product_service.constant.ErrorCodeEnum;
import com.jashan.product_service.dto.ProductRequestDTO;
import com.jashan.product_service.dto.ProductResponseDTO;
import com.jashan.product_service.entity.Product;
import com.jashan.product_service.exception.CustomException;
import com.jashan.product_service.repository.ProductRepository;
import com.jashan.product_service.service.ProductService;

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

    @Override
    public ProductResponseDTO getProductById(Long productId) {
        log.info("Fetching product with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found with id: {}", productId);
                    return new CustomException(
                            ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorCode(),
                            ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorMessage(),
                            HttpStatus.NOT_FOUND,
                            "Product ID not found in DB: " + productId);
                });
        log.debug("Found product: {}", product);

        ProductResponseDTO productResponseDTO = modelMapper.map(product, ProductResponseDTO.class);
        log.debug("Mapped product entity to response DTO: {}", productResponseDTO);

        return productResponseDTO;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        log.info("Fetching all products");

        List<Product> products = productRepository.findAll();
        List<ProductResponseDTO> dtoList = products.stream()
                .map(product -> modelMapper.map(product, ProductResponseDTO.class))
                .collect(Collectors.toList());

        log.debug("Mapped product list to response DTO list: {}", dtoList);
        return dtoList;
    }

    @Override
    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO) {
        log.info("Updating product with ID: {}", productId);

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found with id: {}", productId);
                    return new CustomException(
                            ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorCode(),
                            ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorMessage(),
                            HttpStatus.NOT_FOUND,
                            "Product not found with ID: " + productId);
                });

        modelMapper.map(productRequestDTO, existingProduct);
        existingProduct.setUpdatedAt(LocalDateTime.now());
        log.debug("Mapped request to existing product", existingProduct);

        Product updated = productRepository.save(existingProduct);
        log.info("Updated product: {}", updated);

        ProductResponseDTO productResponseDTO = modelMapper.map(updated, ProductResponseDTO.class);
        log.debug("Mapped the updated product to response dto", productResponseDTO);

        return productResponseDTO;
    }

    @Override
    public void deleteProduct(Long productId) {
        log.info("Deleting product with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found with id: {}", productId);
                    return new CustomException(
                            ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorCode(),
                            ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorMessage(),
                            HttpStatus.NOT_FOUND,
                            "Product not found with ID: " + productId);
                });

        productRepository.delete(product);
        log.info("Deleted product with ID: {}", productId);
    }

}
