package com.vkb.plivosms.exception;

public class PlivoException extends Exception{
    public static final String UNABLE_TO_BASE64_DECODE = "Unable to Base64Decode";
    public static final String USER_LIMIT_REACHED = "User limit reached";

    public PlivoException(Exception e) {
        super(e);
    }

    public PlivoException(String s) {
        super(s);
    }
}
