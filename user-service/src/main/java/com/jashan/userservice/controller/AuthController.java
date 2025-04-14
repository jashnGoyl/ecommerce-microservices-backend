package com.jashan.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jashan.userservice.dto.AuthResponseDTO;
import com.jashan.userservice.dto.LoginRequestDTO;
import com.jashan.userservice.dto.RegisterRequestDTO;
import com.jashan.userservice.pojo.AuthResponse;
import com.jashan.userservice.pojo.LoginRequest;
import com.jashan.userservice.pojo.RegisterRequest;
import com.jashan.userservice.service.impl.UserServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserServiceImpl userServiceImpl;

    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Register endpoint called with email: {}", registerRequest.getEmail());

        log.debug("Mapping register request to DTO");
        RegisterRequestDTO registerRequestDTO = modelMapper.map(registerRequest, RegisterRequestDTO.class);

        log.debug("Calling the register method from service layer");
        AuthResponseDTO authResponseDTO = userServiceImpl.register(registerRequestDTO);
        log.debug("Registration successful for email: {}", authResponseDTO.getEmail());

        log.debug("Mapping AuthResponseDTO to POJO");
        AuthResponse authResponse = modelMapper.map(authResponseDTO, AuthResponse.class);

        ResponseEntity<AuthResponse> response = new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
        log.debug("Sending response from controller layer: " + response);

        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login endpoint called for email: {}", loginRequest.getEmail());

        log.debug("Mapping login request to DTO");
        LoginRequestDTO loginRequestDTO = modelMapper.map(loginRequest, LoginRequestDTO.class);

        log.debug("Calling the login method from service layer");
        AuthResponseDTO authResponseDTO = userServiceImpl.login(loginRequestDTO);
        log.debug("Login successful for email: {}", authResponseDTO.getEmail());

        log.debug("Mapping AuthResponseDTO to POJO");
        AuthResponse authResponse = modelMapper.map(authResponseDTO, AuthResponse.class);

        ResponseEntity<AuthResponse> response = new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
        log.debug("Sending response from controller layer: " + response);

        return response;
    }

}
