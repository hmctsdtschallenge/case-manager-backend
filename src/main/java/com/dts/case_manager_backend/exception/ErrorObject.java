package com.dts.case_manager_backend.exception;

import java.time.LocalDateTime;

public record ErrorObject(
        int statusCode,
        String message,
        LocalDateTime timeStamp) {}