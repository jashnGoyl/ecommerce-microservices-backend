package com.jashan.product_service.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jashan.product_service.constant.ErrorCodeEnum;
import com.jashan.product_service.pojo.ErrorResponse;

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

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        log.debug("Inside JWT filter (Product Service)");

        final String authorizationHeader = request.getHeader("Authorization");

        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                log.error("Authorization header missing or invalid.");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, ErrorCodeEnum.TOKEN_MISSING,
                        request.getMethod(), "Authorization header missing or malformed.");
                return;
            }

            String jwt = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(jwt);

            if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtService.isTokenValid(jwt)) {
                log.error("Token validation failed.");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, ErrorCodeEnum.INVALID_TOKEN,
                        request.getMethod(), "JWT token is not valid.");
                return;
            }

            List<String> roles = jwtService.extractRoles(jwt);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            log.error("Expired token: {}", ex.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, ErrorCodeEnum.TOKEN_EXPIRED,
                    request.getMethod(), ex.getMessage());
        } catch (SignatureException ex) {
            log.error("Invalid signature: {}", ex.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, ErrorCodeEnum.INVALID_SIGNATURE,
                    request.getMethod(), ex.getMessage());
        } catch (MalformedJwtException | UnsupportedJwtException ex) {
            log.error("Malformed or unsupported token: {}", ex.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, ErrorCodeEnum.INVALID_TOKEN,
                    request.getMethod(), ex.getMessage());
        } catch (Exception ex) {
            log.error("Unhandled exception: {}", ex.getMessage());
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeEnum.GENERIC_ERROR,
                    request.getMethod(), ex.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response,
            HttpStatus status,
            ErrorCodeEnum errorCodeEnum,
            String httpMethod,
            String backendMessage) throws IOException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(errorCodeEnum.getErrorCode())
                .errorMessage(errorCodeEnum.getErrorMessage())
                .statusCode(status.value())
                .httpMethod(httpMethod)
                .backendMessage(backendMessage)
                .timestamp(LocalDateTime.now())
                .build();

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.writeValue(response.getWriter(), errorResponse);
    }

}
