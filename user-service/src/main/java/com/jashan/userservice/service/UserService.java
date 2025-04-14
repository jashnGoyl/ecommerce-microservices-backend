package com.jashan.userservice.service;

import com.jashan.userservice.dto.AuthResponseDTO;
import com.jashan.userservice.dto.LoginRequestDTO;
import com.jashan.userservice.dto.RegisterRequestDTO;

public interface UserService {
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO);

    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
}
