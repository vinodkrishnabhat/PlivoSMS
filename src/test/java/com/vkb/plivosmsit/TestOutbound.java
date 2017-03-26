package com.vkb.plivosmsit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestOutbound extends TestCommon{
    /*
    cd C:\Users\Maha\IdeaProjects\PlivoSMS\out\artifacts\AutomationTests_jar
    java -cp AutomationTests.jar com.vkb.plivosmsit.TestOutbound http://localhost:8080
     */

    private String baseURI;

    public TestOutbound() {
        this.baseURI = "http://localhost:8080";
    }

    protected String getURI() {
        return baseURI + "/outbound/sms";
    }
    private String getOutboundAdminResetURI() {
        return baseURI + "/outbound/adminReset";
    }

    public static void main(String[] args) throws Exception {
        TestOutbound testOutbound = new TestOutbound();
        testOutbound.baseURI = args[0];

        testOutbound.runCommonTests();

        testOutbound.validRequest();
        testOutbound.fromNotFound();
        testOutbound.throttleUser();
    }

    @Test
    public void fromNotFound() throws Exception{
        String from = "4924195509196";
        String to = "4924195509198";
        String text = "valid request";
        String username = "plivo2";
        String password = "54P2EOKQ47";

        String[] response = getResponse(from, to, text, username, password);
        assertEquals("from parameter not found", response[1]);
        assertEquals("", response[0]);
    }

    @Test
    public void validRequest() throws Exception{
        String from = "4924195509196";
        String to = "4924195509198";
        String text = "valid request";
        String username = "plivo1";
        String password = "20S0KPNOIM";

        String[] response = getResponse(from, to, text, username, password);
        assertEquals("", response[1]);
        assertEquals("outbound sms ok", response[0]);
    }

    @Test
    public void throttleUser() throws Exception {
        String from = "3253280312";
        String to = "4924195509198";
        String text = "valid request";
        String username = "plivo1";
        String password = "20S0KPNOIM";
        String adminResetURI = getOutboundAdminResetURI();

        String[] response;

        response = getResponse(from, to, "RESET", TestCommon.ADMIN_USER, TestCommon.ADMIN_PASS, adminResetURI);
        assertEquals("", response[1]);
        assertEquals("outbound sms ok", response[0]);

        for(int ctr = 0; ctr < 49; ctr++) {
            response = getResponse(from, to, text, username, password);
            assertEquals("", response[1]);
            assertEquals("outbound sms ok", response[0]);
        }

        response = getResponse(from, to, text, username, password);
        assertEquals("limit reached for from " + from, response[1]);
        assertEquals("", response[0]);

        response = getResponse(from, to, "RESET", TestCommon.ADMIN_USER, TestCommon.ADMIN_PASS, adminResetURI);
        assertEquals("", response[1]);
        assertEquals("outbound sms ok", response[0]);
    }
}
