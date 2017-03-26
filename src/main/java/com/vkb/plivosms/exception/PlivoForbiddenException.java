package com.vkb.plivosms.exception;

import javax.ws.rs.ForbiddenException;

public class PlivoForbiddenException extends ForbiddenException{
    public static final String UNABLE_TO_DECIPHER_AUTH_METHOD = "Unable to decipher auth method";
    public static final String UNABLE_TO_GET_AUTH_ENTITY = "Unable to get auth entity";

    public PlivoForbiddenException(RuntimeException re) {
        super(re);
    }
    public PlivoForbiddenException(String s) {
        super(s);
    }
}
