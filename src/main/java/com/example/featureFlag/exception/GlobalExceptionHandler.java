package com.example.featureFlag.exception;

import com.example.featureFlag.dto.ApiError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        ApiError apiError = new ApiError();
        apiError.setMessage("Feature Flag already exists");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(FeatureFlagNotFoundException.class)
    public ResponseEntity<ApiError> handleFeatureFlagNotFoundException(FeatureFlagNotFoundException e){
        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleInvalidRuleValueException(InvalidRuleValueException e){
        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
