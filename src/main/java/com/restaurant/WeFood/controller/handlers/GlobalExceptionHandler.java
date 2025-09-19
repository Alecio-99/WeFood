package com.restaurant.WeFood.controller.handlers;

import com.restaurant.WeFood.DTO.ResourceNotFoundDTO;
import com.restaurant.WeFood.DTO.ValidationErrorDTO;
import com.restaurant.WeFood.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundDTO> handleRuntimeResourceNotFoundException(ResourceNotFoundException ex) {
         var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new ResourceNotFoundDTO(ex.getMessage(), status.value()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handleRuntimeMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<String>();
        for(var error: ex.getBindingResult().getFieldErrors()){
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        return ResponseEntity.status(status.value()).body(new ValidationErrorDTO(errors, status.value()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ValidationErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = List.of(ex.getMessage());
        return ResponseEntity.status(status.value())
                .body(new ValidationErrorDTO(errors, status.value()));
    }
}
