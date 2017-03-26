package com.vkb.plivosms.utils;

import com.vkb.plivosms.exception.PlivoException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Base64Util {

    static String encode(String str) {
        return new BASE64Encoder().encode(str.getBytes());
    }

    public static String decode(String str) throws PlivoException{
        byte[] bytes;

        try {
            bytes = new BASE64Decoder().decodeBuffer(str);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PlivoException(PlivoException.UNABLE_TO_BASE64_DECODE);
        }

        return new String(bytes);

    }
}
