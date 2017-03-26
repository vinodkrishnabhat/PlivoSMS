package com.vkb.plivosmsit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCombined {
    /*
   cd C:\Users\Maha\IdeaProjects\PlivoSMS\out\artifacts\AutomationTests_jar
   java -cp AutomationTests.jar com.vkb.plivosmsit.TestCombined http://localhost:8080
    */

    private String baseURI;

    public TestCombined() {
        this.baseURI = "http://localhost:8080";
    }

    private String getInboundURI() {
        return baseURI + "/inbound/sms";
    }
    private String getOutboundURI() {
        return baseURI + "/outbound/sms";
    }
    private String getInboundAdminResetURI() {
        return baseURI + "/inbound/adminReset";
    }

    public static void main (String args[]) throws Exception{
        TestCombined testCombined = new TestCombined();
        testCombined.baseURI = args[0];

        testCombined.checkSTOP();
        testCombined.checkSTOPExpiry();
    }

    @Test
    public void checkSTOP() throws Exception {
        String from = "441873440017";
        String to = "441970450009";
        String username = "plivo3";
        String password = "9LLV6I4ZWI";

        inboundStopRequest(from, to, username, password);
        blockedOutboundRequest(from, to, username, password);
        inboundResetRequest(from, to);
    }

    @Test
    public void checkSTOPExpiry() throws Exception {
        String from = "441873440017";
        String to = "441970450009";
        String username = "plivo3";
        String password = "9LLV6I4ZWI";

        inboundStopRequest(from, to, username, password);
        inboundResetRequest(from, to);
        validOutboundRequest(from, to, username, password);
    }

    private void inboundResetRequest(String from, String to) throws Exception{
        String text = "RESET";
        String uri = getInboundAdminResetURI();
        String response[] =  TestCommon.getResponse(from, to, text, TestCommon.ADMIN_USER, TestCommon.ADMIN_PASS, uri);

        assertEquals("", response[1]);
        assertEquals("inbound sms ok", response[0]);
    }

    private void inboundStopRequest(String from, String to, String username, String password) throws Exception{
        String text = "STOP";
        String uri = getInboundURI();
        String response[] =  TestCommon.getResponse(from, to, text, username, password, uri);

        assertEquals("", response[1]);
        assertEquals("inbound sms ok", response[0]);
    }

    private void blockedOutboundRequest(String from, String to, String username, String password) throws Exception{
        String text = "Valid Text";
        String uri = getOutboundURI();
        String response[] =  TestCommon.getResponse(from, to, text, username, password, uri);

        assertEquals("sms from " + from + " to " + to + " blocked by STOP request", response[1]);
        assertEquals("", response[0]);
    }

    private void validOutboundRequest(String from, String to, String username, String password) throws Exception{
        String text = "Valid Text";
        String uri = getOutboundURI();
        String response[] =  TestCommon.getResponse(from, to, text, username, password, uri);

        assertEquals("", response[1]);
        assertEquals("outbound sms ok", response[0]);
    }
}
