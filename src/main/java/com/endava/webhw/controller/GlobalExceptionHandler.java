package com.endava.webhw.controller;

import com.endava.webhw.exception.EntityNotFoundException;
import com.endava.webhw.exception.ErrorResponse;
import oracle.jdbc.OracleDatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String ENTITY_NOT_FOUND = "Entity not found";
    private static final String BAD_PARAMETER_VALUE = "Bad parameter value";
    private static final String BAD_ENTITY = "Entity creation error";
    private static final String CONSTRAINT_VIOLATION = "Constraint Violated";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> onEntityNotFound(HttpServletRequest request, EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                        ENTITY_NOT_FOUND,
                        e.getMessage(),
                        request.getServletPath()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> onConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        List<String> errors = new ArrayList<>();

        for (ConstraintViolation<?> error : e.getConstraintViolations()) {
            errors.add(error.getPropertyPath() + ": ");
            errors.add(error.getMessage() + ".");
        }

        String message = errors.toString();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        CONSTRAINT_VIOLATION,
                        message,
                        request.getServletPath()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> onMethodArgumentTypeMismatch(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
        String message = "Parameter: " + e.getName() + ", has invalid value: " + e.getValue();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        BAD_PARAMETER_VALUE,
                        message,
                        request.getServletPath()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> onMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        String message = errors.toString();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        CONSTRAINT_VIOLATION,
                        message,
                        request.getServletPath()));
    }

    @ExceptionHandler(OracleDatabaseException.class)
    public ResponseEntity<ErrorResponse> onOracleDatabaseException(HttpServletRequest request, OracleDatabaseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        BAD_ENTITY,
                        e.getMessage(),
                        request.getServletPath()));
    }
}