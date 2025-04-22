package com.dts.case_manager_backend.exception;

public class InvalidDTOException extends RuntimeException {
    public InvalidDTOException(String message) {
        super(message);
    }
}
