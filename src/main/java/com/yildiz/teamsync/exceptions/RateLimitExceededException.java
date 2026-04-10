package com.yildiz.teamsync.exceptions;

//429
public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String message){
        super(message);
    }

}
