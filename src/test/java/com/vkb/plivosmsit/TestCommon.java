package com.vkb.plivosmsit;

import org.json.simple.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public abstract class TestCommon {
    protected abstract String getURI();

    static final String ADMIN_USER = "admin";
    static final String ADMIN_PASS = "password";

    String[] getResponse(String from, String to, String text, String username, String password) throws IOException {
        String uri = getURI();
        return getResponse(from, to, text, username, password, uri);
    }

    static String[] getResponse(String from, String to, String text, String username, String password, String uri) throws IOException {
        String requestStr = getJSONString(from, to, text);
        TestRESTClient testRESTClient = new TestRESTClient();
        return testRESTClient.getRESTCallResponse(requestStr, username, password, uri);
    }

    private static String getJSONString(String from, String to, String text) {
        JSONObject obj = new JSONObject();
        obj.put("from", from);
        obj.put("to", to);
        obj.put("text", text);
        return obj.toJSONString();
    }

    void runCommonTests() throws Exception {
        invalidAuth();
        missingParameters();
        invalidParameters();
    }

    @Test
    public void invalidParameters() throws Exception{
        String from = "492419550919";
        String to = "4924195509198";
        String text = "valid request";
        String username = "plivo1";
        String password = "20S0KPNOIM";

        String[] response = getResponse(from + from, to, text, username, password);
        assertEquals("From is invalid", response[1]);
        assertEquals("", response[0]);

        response = getResponse(from, to + to, text, username, password);
        assertEquals("To is invalid", response[1]);
        assertEquals("", response[0]);

        response = getResponse(from, to, "", username, password);
        assertEquals("Text is invalid", response[1]);
        assertEquals("", response[0]);
    }

    @Test
    public void missingParameters() throws Exception {
        String from = "492419550916";
        String to = "4924195509198";
        String text = "valid request";
        String username = "plivo1";
        String password = "20S0KPNOIM";

        String[] response = getResponse(null, to, text, username, password);
        assertEquals("From is missing", response[1]);
        assertEquals("", response[0]);

        response = getResponse(from, null, text, username, password);
        assertEquals("To is missing", response[1]);
        assertEquals("", response[0]);

        response = getResponse(from, to, null, username, password);
        assertEquals("Text is missing", response[1]);
        assertEquals("", response[0]);
    }

    @Test
    public void invalidAuth() throws Exception{
        String from = "4924195509196";
        String to = "4924195509198";
        String text = "valid request";
        String username = "plivo1";
        String password = "wrongPwd";

        String[] response = getResponse(from, to, text, username, password);
        assertEquals("Forbidden", response[1]);
        assertEquals("403", response[0]);
    }
}
