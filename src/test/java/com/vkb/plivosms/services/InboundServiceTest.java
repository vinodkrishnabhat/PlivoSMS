package com.vkb.plivosms.services;

import com.vkb.plivosms.dao.DaoFactoryTest;
import com.vkb.plivosms.objects.SmsRequestTO;
import com.vkb.plivosms.objects.SmsResponseTO;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.ForbiddenException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InboundServiceTest extends SMSServiceTest {
    private InboundService inboundService;

    @Before
    public void setUp() throws Exception {
        this.inboundService = new InboundService();
        super.setUp(this.inboundService);

        inboundService.setBlockedPairDao(DaoFactoryTest.getBlockedPairDao());
        inboundService.setPhoneNumberDao(DaoFactoryTest.getPhoneNumberDao());
    }

    @Test (expected= ForbiddenException.class)
    public void authFail() throws Exception {
        //"plivo1:20S0KPNOI"
        this.inboundService.handleSMS(null, "Basic cGxpdm8xOjIwUzBLUE5PSQ==");
    }

    @Test
    public void basicValidationFail() throws Exception {
        String from = "9199";
        String to = "919911111111";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.inboundService.handleSMS(smsRequestTO, authString);
        assertNotNull(response);
        assertEquals("", response.getMessage());
        assertEquals("From is invalid", response.getError());
    }

    @Test
    public void missingToValidationFail() throws Exception {
        String from = "919900000000";
        String to = "919911111111";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.inboundService.handleSMS(smsRequestTO, authString);
        assertNotNull(response);
        assertEquals("", response.getMessage());
        assertEquals("to parameter not found", response.getError());
    }

    @Test
    public void nonSTOPRequest() throws Exception {
        String from = "919900000000";
        String to = "4924195509198";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.inboundService.handleSMS(smsRequestTO, authString);
        assertNotNull(response);
        assertEquals("inbound sms ok", response.getMessage());
        assertEquals("", response.getError());
    }

    @Test
    public void STOPRequest() throws Exception {
        String text = "STOP";
        processSTOPRequest(text);

        text = "STOP\r";
        processSTOPRequest(text);

        text = "STOP\n";
        processSTOPRequest(text);

        text = "STOP\r\n";
        processSTOPRequest(text);
    }

    private void processSTOPRequest(String text) {
        String from = "4924195509196";
        String to = "4924195509198";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.inboundService.handleSMS(smsRequestTO, authString);
        assertNotNull(response);
        assertEquals("inbound sms ok", response.getMessage());
        assertEquals("", response.getError());
    }

}