package com.example.exception;

public class AccountLockedException extends UserException {
    public AccountLockedException(String message) {
        super(403, message);
    }
} 