package com.kostenko.demo.proxy.seller.error;

import com.kostenko.demo.proxy.seller.dto.ApplicationErrorDTO;
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
    public ResponseEntity<ApplicationErrorDTO> catchResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new ApplicationErrorDTO(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler
    public ResponseEntity<ApplicationErrorDTO> catchUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new ApplicationErrorDTO(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }


    @ExceptionHandler
    public ResponseEntity<ApplicationErrorDTO> catchIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new ApplicationErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler
    public ResponseEntity<ApplicationErrorDTO> catchAccessDeniedExceptionException(AccessDeniedException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new ApplicationErrorDTO(HttpStatus.FORBIDDEN.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}