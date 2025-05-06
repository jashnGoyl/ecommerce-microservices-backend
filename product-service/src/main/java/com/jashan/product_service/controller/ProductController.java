package com.jashan.product_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jashan.product_service.dto.ProductRequestDTO;
import com.jashan.product_service.dto.ProductResponseDTO;
import com.jashan.product_service.pojo.ProductRequest;
import com.jashan.product_service.pojo.ProductResponse;
import com.jashan.product_service.service.impl.ProductServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    private final ModelMapper modelMapper;

    @PostMapping("/admin/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        log.info("Received request to create product: {}", productRequest.getName());

        ProductRequestDTO productRequestDTO = modelMapper.map(productRequest, ProductRequestDTO.class);
        log.debug("Mapped request dto from request pojo: {}", productRequestDTO);

        ProductResponseDTO productResponseDTO = productServiceImpl.createProduct(productRequestDTO);
        log.debug("Recieved response from service: {}", productResponseDTO);

        ProductResponse productResponse = modelMapper.map(productResponseDTO, ProductResponse.class);
        log.debug("Mapped dto reponse to pojo", productResponse);

        ResponseEntity<ProductResponse> response = new ResponseEntity<ProductResponse>(productResponse,
                HttpStatusCode.valueOf(201));
        log.debug("Response ready to be sent to client: {}", response);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("Fetching product by ID: {}", id);

        ProductResponseDTO productDTO = productServiceImpl.getProductById(id);
        log.debug("Recieved response dto in controller", productDTO);

        ProductResponse productResponse = modelMapper.map(productDTO, ProductResponse.class);
        log.debug("Mapped dto response to pojo", productResponse);

        return ResponseEntity.ok(productResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("Fetching all products");

        List<ProductResponseDTO> allProducts = productServiceImpl.getAllProducts();
        log.debug("Recived a list of products: []", allProducts);

        List<ProductResponse> responses = allProducts.stream()
                .map(productDTO -> modelMapper.map(productDTO, ProductResponse.class))
                .collect(Collectors.toList());
        log.debug("Mapped the recieved list to response: []", responses);

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
            @RequestBody @Valid ProductRequest request) {
        log.info("Updating product ID: {}", id);

        ProductRequestDTO productRequestDTO = modelMapper.map(request, ProductRequestDTO.class);
        log.debug("Mapped pojo request to dto", productRequestDTO);

        ProductResponseDTO productReponseDTO = productServiceImpl.updateProduct(id, productRequestDTO);
        log.debug("Received response from service layer: {}", productReponseDTO);

        ProductResponse response = modelMapper.map(productReponseDTO, ProductResponse.class);
        log.debug("Mapped dto response to pojo: {}", response);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product ID: {}", id);

        productServiceImpl.deleteProduct(id);

        log.info("Deleted product with ID: {}", id);

        return ResponseEntity.noContent().build();
    }

}
