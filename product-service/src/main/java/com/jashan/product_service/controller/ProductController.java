package com.jashan.product_service.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jashan.product_service.dto.ProductRequestDTO;
import com.jashan.product_service.dto.ProductResponseDTO;
import com.jashan.product_service.pojo.ProductRequest;
import com.jashan.product_service.pojo.ProductResponse;
import com.jashan.product_service.service.ProductServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    private final ModelMapper modelMapper;

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
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
}
