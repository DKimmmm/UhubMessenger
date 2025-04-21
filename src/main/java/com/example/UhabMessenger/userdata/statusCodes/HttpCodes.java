package com.example.UhabMessenger.userdata.statusCodes;

public enum HttpCodes {
    AlreadyUserExists(420),
    UncorrectedPassword(421),
    AuthorizationError(422);

    private final Integer code;

    HttpCodes(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
