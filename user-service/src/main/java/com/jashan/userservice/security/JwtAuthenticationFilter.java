package com.jashan.userservice.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jashan.userservice.constant.ErrorCodeEnum;
import com.jashan.userservice.exception.CustomException;
import com.jashan.userservice.service.impl.CustomUserDetailService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    private CustomUserDetailService userDetailService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailService userDetailService) {
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("Inside JWT filter");
        try {
            log.info("Inside JWT filter and doFilterInteral method");
            final String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null && !(request.getRequestURI().endsWith("login")
                    || request.getRequestURI().endsWith("register"))) {
                throw new IllegalArgumentException();
            }

            String username = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtService.extractUsername(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                if (jwtService.validateToken(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            log.error("Throwing ExpiredJwtException.");
            throw new CustomException(
                    ErrorCodeEnum.TOKEN_EXPIRED.getErrorCode(), ErrorCodeEnum.TOKEN_EXPIRED.getErrorMessage(),
                    HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (@SuppressWarnings("deprecation") SignatureException ex) {
            log.error("Throwing SignatureException.");
            throw new CustomException(
                    ErrorCodeEnum.INVALID_SIGNATURE.getErrorCode(), ErrorCodeEnum.INVALID_SIGNATURE.getErrorMessage(),
                    HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (MalformedJwtException | UnsupportedJwtException ex) {
            log.error("Throwing MalformedJwtException | UnsupportedJwtException.");
            throw new CustomException(
                    ErrorCodeEnum.INVALID_TOKEN.getErrorCode(), ErrorCodeEnum.INVALID_TOKEN.getErrorMessage(),
                    HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Throwing IllegalArgumentException.");
            throw new CustomException(
                    ErrorCodeEnum.TOKEN_MISSING.getErrorCode(), ErrorCodeEnum.TOKEN_MISSING.getErrorMessage(),
                    HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (Exception ex) {
            log.error("Throwing Exception.");
            throw new CustomException(
                    ErrorCodeEnum.GENERIC_ERROR.getErrorCode(), ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

}
