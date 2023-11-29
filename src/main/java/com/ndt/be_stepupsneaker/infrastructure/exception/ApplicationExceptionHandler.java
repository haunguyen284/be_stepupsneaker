package com.ndt.be_stepupsneaker.infrastructure.exception;

import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {


    // valid entity
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handlerInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    // Not found
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handlerResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        return ResponseHelper.getErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    //sever
//        @ExceptionHandler(Exception.class)
//        public ResponseEntity<?> handlerGlobalException(Exception exception, WebRequest request) {
//            return ResponseHelper.getErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handlerAPIException(ApiException exception, WebRequest request) {
        return ResponseHelper.getErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseHelper.getErrorResponse(ex.getMessage(),HttpStatus.FORBIDDEN);
    }

}
