package com.vkb.plivosms.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class Base64UtilTest {
    @Test(expected=NullPointerException.class)
    public void encodeNull() throws Exception {
        Base64Util.encode(null);
    }

    @Test
    public void encode() throws Exception {
        String result = Base64Util.encode("test");
        assertEquals("dGVzdA==", result);
    }

    @Test(expected=NullPointerException.class)
    public void decodeNull() throws Exception {
        Base64Util.decode(null);
    }

    @Test
    public void decode() throws Exception {
        String result = Base64Util.decode("dGVzdA==");
        assertEquals("test", result);
    }


}