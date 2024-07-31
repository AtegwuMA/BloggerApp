package com.blogger.blogger.enums;

public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    CUSTOM_CODE(555);

    private final int code;

    HttpStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}