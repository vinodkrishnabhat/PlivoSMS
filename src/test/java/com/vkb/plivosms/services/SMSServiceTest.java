package com.vkb.plivosms.services;

import com.vkb.plivosms.dao.DaoFactoryTest;
import com.vkb.plivosms.exception.PlivoForbiddenException;
import com.vkb.plivosms.objects.AccountEntity;
import com.vkb.plivosms.objects.SmsRequestTO;
import com.vkb.plivosms.objects.SmsResponseTO;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class SMSServiceTest {
    private SMSService smsService;

    void setUp(SMSService smsService) throws Exception {
        this.smsService = smsService;
        this.smsService.setAccountDao(DaoFactoryTest.getAccountDao());
    }


    @Test
    public void validateParams() throws Exception {
        String from = "919900000000";
        String to = "919911111111";
        String text = "Valid message";

        SmsRequestTO smsRequestTO = getSmsRequestTo(to, text, from);
        SmsResponseTO smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNull(smsResponseTO);
    }

    @Test
    public void validateInvalidFrom() throws Exception {
        String to = "919911111111";
        String text = "Valid message";

        SmsRequestTO smsRequestTO = getSmsRequestTo(to, text, "12345");
        SmsResponseTO smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNotNull(smsResponseTO);
        assertEquals("", smsResponseTO.getMessage());
        assertEquals("From is invalid", smsResponseTO.getError());

        smsRequestTO = getSmsRequestTo(to, text, "12345678 12345678");
        smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNotNull(smsResponseTO);
        assertEquals("", smsResponseTO.getMessage());
        assertEquals("From is invalid", smsResponseTO.getError());

        smsRequestTO = getSmsRequestTo(to, text, null);
        smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNotNull(smsResponseTO);
        assertEquals("", smsResponseTO.getMessage());
        assertEquals("From is missing", smsResponseTO.getError());
    }

    @Test
    public void validateInvalidTo() throws Exception {
        String from = "919911111111";
        String text = "Valid message";

        SmsRequestTO smsRequestTO = getSmsRequestTo("12345", text, from);
        SmsResponseTO smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNotNull(smsResponseTO);
        assertEquals("", smsResponseTO.getMessage());
        assertEquals("To is invalid", smsResponseTO.getError());

        smsRequestTO = getSmsRequestTo("12345678 12345678", text, from);
        smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNotNull(smsResponseTO);
        assertEquals("", smsResponseTO.getMessage());
        assertEquals("To is invalid", smsResponseTO.getError());

        smsRequestTO = getSmsRequestTo(null, text, from);
        smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNotNull(smsResponseTO);
        assertEquals("", smsResponseTO.getMessage());
        assertEquals("To is missing", smsResponseTO.getError());
    }

    @Test
    public void validateInvalidText() throws Exception {
        String to = "919911111111";
        String from = "919911111111";

        SmsRequestTO smsRequestTO = getSmsRequestTo(to, "", from);
        SmsResponseTO smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNotNull(smsResponseTO);
        assertEquals("", smsResponseTO.getMessage());
        assertEquals("Text is invalid", smsResponseTO.getError());

        smsRequestTO = getSmsRequestTo(to, "0123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 ", from);
        smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNotNull(smsResponseTO);
        assertEquals("", smsResponseTO.getMessage());
        assertEquals("Text is invalid", smsResponseTO.getError());

        smsRequestTO = getSmsRequestTo(to, null, from);
        smsResponseTO = this.smsService.validateParams(smsRequestTO);
        assertNotNull(smsResponseTO);
        assertEquals("", smsResponseTO.getMessage());
        assertEquals("Text is missing", smsResponseTO.getError());
    }

    SmsRequestTO getSmsRequestTo(String to, String text, String from) {
        SmsRequestTO smsRequestTO = new SmsRequestTO();
        smsRequestTO.setTo(to);
        smsRequestTO.setText(text);
        smsRequestTO.setFrom(from);
        return smsRequestTO;
    }

    @Test(expected=PlivoForbiddenException.class)
    public void authenticateNullString() throws Exception {
        this.smsService.isUserAuthenticated(null);
    }

    @Test(expected=PlivoForbiddenException.class)
    public void authenticateInvalidString() throws Exception {
        this.smsService.isUserAuthenticated("testdGVzdA==");
    }

    @Test(expected=PlivoForbiddenException.class)
    public void authenticateInvalidFormatString() throws Exception {
        //"plivo120S0KPNOIM"
        this.smsService.isUserAuthenticated("test cGxpdm8xMjBTMEtQTk9JTQ==");
    }

    @Test
    public void authenticateInvalidPassword() throws Exception {
        //"plivo1:20S0KPNOI"
        AccountEntity accountEntity = this.smsService.isUserAuthenticated("Basic cGxpdm8xOjIwUzBLUE5PSQ==");
        assertNull(accountEntity);
    }

    @Test
    public void authenticateValidPassword() throws Exception {
        //"plivo1:20S0KPNOIM"
        AccountEntity accountEntity = this.smsService.isUserAuthenticated("Basic cGxpdm8xOjIwUzBLUE5PSU0==");
        assertNotNull(accountEntity);
    }
}
