package com.vicarius.vicariuschallenge.handler;

import com.vicarius.vicariuschallenge.exceptions.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {RateLimitExceededException.class})
    public ResponseEntity<ErrorMessage> handlerResourceNotFoundException(RateLimitExceededException e) {

        return new ResponseEntity<>(ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title(e.getMessage())
                .message(e.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

}
