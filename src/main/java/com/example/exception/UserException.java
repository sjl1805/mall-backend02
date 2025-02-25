package com.example.exception;

public class UserException extends RuntimeException {
    private final int code;

    public UserException(String message) {
        super(message);
        this.code = 500;
    }

    public UserException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}