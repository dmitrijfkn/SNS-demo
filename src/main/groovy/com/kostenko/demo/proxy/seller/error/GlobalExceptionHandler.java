package com.kostenko.demo.proxy.seller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new ApplicationError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new ApplicationError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }


    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchAccessDeniedExceptionException(AccessDeniedException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new ApplicationError(HttpStatus.FORBIDDEN.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}