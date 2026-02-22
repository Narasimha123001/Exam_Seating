package com.techtricks.coe_auth.exceptions;

public class UserAlreadyPresentException extends Throwable {
    public UserAlreadyPresentException(String message) {
        super(message);
    }
}
