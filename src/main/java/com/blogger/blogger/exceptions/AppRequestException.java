package com.blogger.blogger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AppRequestException extends RuntimeException {
    public AppRequestException() {
        super();
    }

    public AppRequestException(String message) {
        super(message);
    }

    public AppRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}