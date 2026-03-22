package com.yildiz.teamsync.exceptions;

// 403
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
