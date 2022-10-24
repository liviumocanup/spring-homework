package com.endava.webhw.controller;

import com.endava.webhw.exception.EntityNotFoundException;
import com.endava.webhw.exception.ErrorResponse;
import oracle.jdbc.OracleDatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String ENTITY_NOT_FOUND = "Entity not found";
    private static final String BAD_PARAMETER_VALUE = "Bad parameter value";

    private static final String BAD_ENTITY = "Entity creation error";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> onEntityNotFound(HttpServletRequest request, EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                        ENTITY_NOT_FOUND,
                        e.getMessage(),
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

    @ExceptionHandler(OracleDatabaseException.class)
    public ResponseEntity<ErrorResponse> onOracleDatabaseException(HttpServletRequest request, OracleDatabaseException e) {
        //String message = "Parameter: " + e.getName() + ", has invalid value: " + e.getValue();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        BAD_ENTITY,
                        e.getMessage(),
                        request.getServletPath()));
    }
}