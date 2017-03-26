package com.vkb.plivosmsit;

public class TestAll {
    /*
    cd C:\Users\Maha\IdeaProjects\PlivoSMS\out\artifacts\AutomationTests_jar
    java -cp PlivoSMS_IT.jar com.vkb.plivosmsit.TestAll http://localhost:8080
     */

    public static void main(String[] args) throws Exception{
        String baseURI = args[0];

        System.out.println("About to run Inbound tests");
        TestInbound.main(new String[] {baseURI});

        System.out.println("About to run Outbound tests");
        TestOutbound.main(new String[] {baseURI});

        System.out.println("About to run Combined tests");
        TestCombined.main(new String[] {baseURI});
    }
}
