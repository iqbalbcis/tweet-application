package com.main.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( value = Exception.class) // this one is generic and global for all
    public final ResponseEntity<Object> handleAllException
            (Exception ex, WebRequest request) throws Exception {

        log.error("error: {}", ex.getMessage(), ex);

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                ex.getMessage(), null, request.getDescription(false));
        return new ResponseEntity<>
                (exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class) // custom exception
    public final ResponseEntity<Object> handleNotFoundException
            (ResourceNotFoundException ex, WebRequest request) throws Exception {

        log.error("error: {}", ex.getMessage(), ex);

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                ex.getMessage(), null, request.getDescription(false));
        return new ResponseEntity<>
                (exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException ex,
             HttpHeaders headers,
             HttpStatusCode status,
             WebRequest request) {
        log.error("error: {}", ex.getMessage(), ex);

        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errorMap.put(err.getField(), err.getDefaultMessage());
        });

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                null, errorMap, request.getDescription(false));
        return new ResponseEntity<>
                (exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
