package com.aeroflex.identityservice.exception;

import com.aeroflex.common.exception.ConflictException;
import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<String>> handleConflict(
            ConflictException ex,
            HttpServletRequest request) {

        logger.error("ConflictException: {}",ex.getMessage(),ex);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialException(
            BadCredentialsException ex,
            HttpServletRequest request) {
        logger.error("BadCredentialsException: {}",ex.getMessage(),ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Invalid username or password"));
    }

    @ExceptionHandler(InvalidPasswordResetTokenException.class)
    public ResponseEntity<ApiResponse<String>>
    handleInvalidPasswordResetToken(
            InvalidPasswordResetTokenException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.error(
                                "Invalid or expired password reset token"
                        )
                );
    }
}