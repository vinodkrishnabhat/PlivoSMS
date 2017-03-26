package com.vkb.plivosmsit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestInbound extends TestCommon{
    /*
    cd C:\Users\Maha\IdeaProjects\PlivoSMS\out\artifacts\AutomationTests_jar
    java -cp AutomationTests.jar com.vkb.plivosmsit.TestInbound http://localhost:8080
     */

    private String baseURI;

    public TestInbound() {
        this.baseURI = "http://localhost:8080";
    }

    protected String getURI() {
        return baseURI + "/inbound/sms";
    }

    public static void main(String[] args) throws Exception {
        TestInbound testInbound = new TestInbound();
        testInbound.baseURI = args[0];

        testInbound.runCommonTests();

        testInbound.validRequest();
        testInbound.toNotFound();
    }

    @Test
    public void toNotFound() throws Exception{
        String from = "4924195509196";
        String to = "4924195509198";
        String text = "valid request";
        String username = "plivo2";
        String password = "54P2EOKQ47";

        String[] response = getResponse(from, to, text, username, password);
        assertEquals("to parameter not found", response[1]);
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
        assertEquals("inbound sms ok", response[0]);
    }


}
