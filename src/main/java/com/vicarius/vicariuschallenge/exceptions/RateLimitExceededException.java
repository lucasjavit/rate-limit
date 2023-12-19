package com.vicarius.vicariuschallenge.exceptions;

public class RateLimitExceededException extends RuntimeException{

    public RateLimitExceededException(String message) {
        super(message);
    }
}
