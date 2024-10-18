package com.student.management.system.exception;

public class RegistrationAlreadyExistsException extends RuntimeException {
    public RegistrationAlreadyExistsException(String message) {
        super(message);
    }
}