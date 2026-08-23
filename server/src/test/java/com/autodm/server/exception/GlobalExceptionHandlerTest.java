package com.autodm.server.exception;

import com.autodm.server.dto.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidationExceptions_ShouldReturnBadRequestWithErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("dto", "name", "Name cannot be null"),
                new FieldError("dto", "level", "Level must be greater than 0")
        ));

        ResponseEntity<ApiErrorResponse> responseEntity = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation failed", responseEntity.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getBody().getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), responseEntity.getBody().getError());

        assertNotNull(responseEntity.getBody().getValidationErrors());
        assertEquals(2, responseEntity.getBody().getValidationErrors().size());
        assertEquals("Name cannot be null", responseEntity.getBody().getValidationErrors().get("name"));
        assertEquals("Level must be greater than 0", responseEntity.getBody().getValidationErrors().get("level"));
    }

    @Test
    void handleResponseStatusException_ShouldReturnProvidedStatusAndReason() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");

        ResponseEntity<ApiErrorResponse> responseEntity = handler.handleResponseStatusException(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Entity not found", responseEntity.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getBody().getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), responseEntity.getBody().getError());
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<ApiErrorResponse> responseEntity = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("An unexpected error occurred", responseEntity.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getBody().getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), responseEntity.getBody().getError());
    }
}
