package com.jashan.userservice.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jashan.userservice.constant.ErrorCodeEnum;
import com.jashan.userservice.entity.User;
import com.jashan.userservice.exception.CustomException;
import com.jashan.userservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user from DB by username/email: {}", username);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    return new CustomException(
                            ErrorCodeEnum.USER_NOT_FOUND.getErrorCode(),
                            ErrorCodeEnum.USER_NOT_FOUND.getErrorMessage(),
                            HttpStatus.NOT_FOUND,
                            "User with email '" + username + "' was not found in the database.");
                });
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

}
