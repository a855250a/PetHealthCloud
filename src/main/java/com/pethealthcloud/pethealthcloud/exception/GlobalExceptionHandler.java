package com.pethealthcloud.pethealthcloud.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.pethealthcloud.pethealthcloud.exception.PetNotFoundException;
import com.pethealthcloud.pethealthcloud.exception.ForbiddenException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{
            errors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        });

        return errors;

    }

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlePetNotFoundException(
            PetNotFoundException ex) {
        Map<String, String> error = new HashMap<>();

        error.put("message", ex.getMessage());
        return error;
    }
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleForbiddenException(
            ForbiddenException ex) {
        Map<String,String> error = new HashMap<>();

        error.put("message", ex.getMessage());

        return error;
    }
}
