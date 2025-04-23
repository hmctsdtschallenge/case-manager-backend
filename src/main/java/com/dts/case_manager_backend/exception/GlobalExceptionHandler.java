package com.dts.case_manager_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorObject> handleAlbumNotFoundException(TaskNotFoundException e){

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ErrorObject errorObject = new ErrorObject(httpStatus.value(),e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(errorObject, httpStatus);
    }

    @ExceptionHandler(InvalidDTOException.class)
    public ResponseEntity<ErrorObject> handleInvalidDTOException(InvalidDTOException e){

        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        ErrorObject errorObject = new ErrorObject(httpStatus.value(),e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(errorObject, httpStatus);
    }
}
