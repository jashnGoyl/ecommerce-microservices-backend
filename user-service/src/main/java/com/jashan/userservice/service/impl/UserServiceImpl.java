package com.jashan.userservice.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jashan.userservice.constant.ErrorCodeEnum;
import com.jashan.userservice.dto.AuthResponseDTO;
import com.jashan.userservice.dto.LoginRequestDTO;
import com.jashan.userservice.dto.RegisterRequestDTO;
import com.jashan.userservice.entity.User;
import com.jashan.userservice.exception.CustomException;
import com.jashan.userservice.repository.UserRepository;
import com.jashan.userservice.security.JwtService;
import com.jashan.userservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtService jwtService;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {

        log.info("Attempting to register user with email: {}", registerRequestDTO.getEmail());

        Optional<User> existing = userRepository.findByEmail(registerRequestDTO.getEmail());
        if (existing.isPresent()) {
            log.warn("Registration failed: Email already exists - {}", registerRequestDTO.getEmail());
            throw new CustomException(
                    ErrorCodeEnum.USER_ALREADY_EXISTS.getErrorCode(),
                    ErrorCodeEnum.USER_ALREADY_EXISTS.getErrorMessage(),
                    HttpStatus.BAD_REQUEST,
                    "User with email already exists in the system");
        }

        log.debug("Mapping RegisterRequest to User entity");
        User user = modelMapper.map(registerRequestDTO, User.class);

        log.debug("Encoding user password");
        user.setPassword(bCryptPasswordEncoder.encode(registerRequestDTO.getPassword()));

        user.setRole(User.Role.USER);

        log.debug("Saving user to database: {}", user);
        User savedUser = userRepository.save(user);

        log.info("User registered successfully with email: {}", savedUser.getEmail());

        String token = jwtService.generateToken(savedUser.getEmail());

        return AuthResponseDTO.builder()
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .token(token)
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        log.info("User login attempt with email: {}", loginRequestDTO.getEmail());

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> {
                    log.error("Login failed: Email not found - {}", loginRequestDTO.getEmail());
                    return new CustomException(
                            ErrorCodeEnum.USER_NOT_FOUND.getErrorCode(),
                            ErrorCodeEnum.USER_NOT_FOUND.getErrorMessage(),
                            HttpStatus.NOT_FOUND,
                            "User with email '" + loginRequestDTO.getEmail() + "' not found");
                });

        log.debug("Verifying password for user: {}", user.getEmail());

        if (!bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            log.error("Login failed: Email not found - {}", loginRequestDTO.getEmail());
            throw new CustomException(
                    ErrorCodeEnum.INVALID_CREDENTIALS.getErrorCode(),
                    ErrorCodeEnum.INVALID_CREDENTIALS.getErrorMessage(),
                    HttpStatus.UNAUTHORIZED,
                    "Incorrect password provided for email: " + loginRequestDTO.getEmail());
        }

        log.info("User logged in successfully: {}", user.getEmail());

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponseDTO.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .build();
    }

}
