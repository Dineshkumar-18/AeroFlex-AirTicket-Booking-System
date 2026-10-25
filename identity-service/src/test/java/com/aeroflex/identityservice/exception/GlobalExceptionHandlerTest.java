package com.aeroflex.identityservice.exception;

import com.aeroflex.common.exception.BusinessException;
import com.aeroflex.common.exception.ConflictException;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.common.exception.UnauthorizedException;
import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.common.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("User not found", response.getBody().getMessage());
    }

    @Test
    void shouldHandleBadRequestException() {
        BadRequestException ex = new BadRequestException("Current password is incorrect.");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleBadRequest(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Current password is incorrect.", response.getBody().getMessage());
    }

    @Test
    void shouldHandleConflictException() {
        ConflictException ex = new ConflictException("Email already exists.");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleConflict(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Email already exists.", response.getBody().getMessage());
    }

    @Test
    void shouldHandleBadCredentialsException() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleBadCredentialException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid username or password", response.getBody().getMessage());
    }

    @Test
    void shouldHandleUnauthorizedException() {
        UnauthorizedException ex = new UnauthorizedException("User is not authenticated.");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleUnauthorized(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("User is not authenticated.", response.getBody().getMessage());
    }

    @Test
    void shouldHandleAccessDeniedException() {
        AccessDeniedException ex = new AccessDeniedException("User is not authorized for this login");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleAccessDenied(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("User is not authorized for this login", response.getBody().getMessage());
    }

    @Test
    void shouldHandleBusinessException() {
        BusinessException ex = new BusinessException("MFA already enabled");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleBusinessException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("MFA already enabled", response.getBody().getMessage());
    }

    @Test
    void shouldHandleGeneralException() {
        RuntimeException ex = new RuntimeException("Unexpected database failure");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Unexpected database failure", response.getBody().getMessage());
    }
}
