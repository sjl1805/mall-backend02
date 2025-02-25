package com.example.exception;

/**
 * 用户名已存在异常
 * <p>
 * 当尝试注册一个已经存在的用户名时抛出此异常。
 * 用于用户注册流程中的用户名唯一性检查。
 */
public class UsernameExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsernameExistsException() {
        super();
    }

    public UsernameExistsException(String message) {
        super(message);
    }

    public UsernameExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

