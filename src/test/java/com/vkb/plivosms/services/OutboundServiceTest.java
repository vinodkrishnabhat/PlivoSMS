package com.vkb.plivosms.services;

import com.vkb.plivosms.dao.DaoFactoryTest;
import com.vkb.plivosms.objects.SmsRequestTO;
import com.vkb.plivosms.objects.SmsResponseTO;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.ForbiddenException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OutboundServiceTest extends SMSServiceTest {
    private OutboundService outboundService;

    @Before
    public void setUp() throws Exception {
        this.outboundService = new OutboundService();
        super.setUp(this.outboundService);

        outboundService.setBlockedPairDao(DaoFactoryTest.getBlockedPairDao());
        outboundService.setPhoneNumberDao(DaoFactoryTest.getPhoneNumberDao());
        outboundService.setSmsCountDao(DaoFactoryTest.getSmsCountDao());
    }

    @Test (expected= ForbiddenException.class)
    public void authFail() throws Exception {
        //"plivo1:20S0KPNOI"
        this.outboundService.handleSMS(null, "Basic cGxpdm8xOjIwUzBLUE5PSQ==");
        this.outboundService.handleSMS(null, "NonBasic cGxpdm8xOjIwUzBLUE5PSQ==");
    }

    @Test
    public void basicValidationFail() throws Exception {
        String from = "919900000000";
        String to = "91991";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.outboundService.handleSMS(smsRequestTO, authString);
        assertNotNull(response);
        assertEquals("", response.getMessage());
        assertEquals("To is invalid", response.getError());
    }

    @Test
    public void missingFromValidationFail() throws Exception {
        String from = "919900000000";
        String to = "919911111111";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.outboundService.handleSMS(smsRequestTO, authString);
        assertNotNull(response);
        assertEquals("", response.getMessage());
        assertEquals("from parameter not found", response.getError());
    }

    @Test
    public void testBlockedPair() throws Exception {
        String from = "4924195509196";
        String to = "4924195509198";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.outboundService.handleSMS(smsRequestTO, authString);
        assertNotNull(response);
        assertEquals("", response.getMessage());
        assertEquals("sms from 4924195509196 to 4924195509198 blocked by STOP request", response.getError());
    }

    @Test
    public void testFirstTimeUserThrottle() throws Exception {
        String from = "firsttimeuser";
        String to = "4924195509196";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.outboundService.handleSMS(smsRequestTO, authString);
        assertEquals("", response.getError());
        assertEquals("outbound sms ok", response.getMessage());
    }

    @Test
    public void testReturningUserThrottleAllow() throws Exception {
        String from = "returninguser1";
        String to = "4924195509196";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.outboundService.handleSMS(smsRequestTO, authString);
        assertEquals("", response.getError());
        assertEquals("outbound sms ok", response.getMessage());
    }

    @Test
    public void testReturningUserThrottleDeny() throws Exception {
        String from = "returninguser2";
        String to = "4924195509196";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.outboundService.handleSMS(smsRequestTO, authString);
        assertEquals("", response.getMessage());
        assertEquals("limit reached for from " + from, response.getError());
    }

    @Test
    public void testReturningUserThrottleOutOfWindowBeforeTime() throws Exception {
        String from = "returninguser3";
        String to = "4924195509196";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.outboundService.handleSMS(smsRequestTO, authString);
        assertEquals("outbound sms ok", response.getMessage());
        assertEquals("", response.getError());
    }

    @Test
    public void testReturningUserThrottleOutOfWindowAfterTime() throws Exception {
        String from = "returninguser4";
        String to = "4924195509196";
        String text = "Valid message";

        //"plivo1:20S0KPNOIM"
        String authString = "Basic cGxpdm8xOjIwUzBLUE5PSU0==";
        SmsRequestTO smsRequestTO = super.getSmsRequestTo(to, text, from);

        SmsResponseTO response = this.outboundService.handleSMS(smsRequestTO, authString);
        assertEquals("outbound sms ok", response.getMessage());
        assertEquals("", response.getError());
    }

}