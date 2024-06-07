package com.marcusmonteirodesouza.realworld.api.exceptionhandlers;

import com.marcusmonteirodesouza.realworld.api.exceptionhandlers.dto.ErrorResponse;
import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse;
        HttpStatusCode statusCode;

        if (ex instanceof AlreadyExistsException) {
            errorResponse = new ErrorResponse(new String[] {ex.getMessage()});
            statusCode = HttpStatus.CONFLICT;
        } else if (ex instanceof IllegalArgumentException) {
            errorResponse = new ErrorResponse(new String[] {ex.getMessage()});
            statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (ex instanceof NotFoundException) {
            errorResponse = new ErrorResponse(new String[] {ex.getMessage()});
            statusCode = HttpStatus.NOT_FOUND;
        } else {
            errorResponse = new ErrorResponse(new String[] {"Internal Server Error"});
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), statusCode, request);
    }
}
