package com.vkb.plivosms.validators;

import com.vkb.plivosms.constants.ResponseMessagesKeys;
import com.vkb.plivosms.objects.SmsRequestTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateSmsTOTest {

    private ValidateSmsTO validateSmsTO;

    @Before
    public void setUp() throws Exception {
        this.validateSmsTO = new ValidateSmsTO();
    }

    @Test
    public void validate() throws Exception {
        String from = "919900000000";
        String text = "Valid message";
        String to = "919911111111";

        SmsRequestTO smsRequestTO = getSmsRequestTO(from, text, to);

        boolean result = this.validateSmsTO.validate(smsRequestTO);
        assertTrue(result);
        assertEquals("", validateSmsTO.getErrorKey());
    }

    @Test
    public void validateFromMin() throws Exception {
        String from = "12345";
        String text = "Valid message";
        String to = "919911111111";

        SmsRequestTO smsRequestTO = getSmsRequestTO(from, text, to);

        boolean result = this.validateSmsTO.validate(smsRequestTO);
        assertFalse(result);
        assertEquals(ResponseMessagesKeys.INVALID_FROM, validateSmsTO.getErrorKey());
    }

    @Test
    public void validateFromMax() throws Exception {
        String from = "12345678 12345678";
        String text = "Valid message";
        String to = "919911111111";

        SmsRequestTO smsRequestTO = getSmsRequestTO(from, text, to);

        boolean result = this.validateSmsTO.validate(smsRequestTO);
        assertFalse(result);
        assertEquals(ResponseMessagesKeys.INVALID_FROM, validateSmsTO.getErrorKey());
    }

    @Test
    public void validateToMin() throws Exception {
        String from = "919900000000";
        String text = "Valid message";
        String to = "12345";

        SmsRequestTO smsRequestTO = getSmsRequestTO(from, text, to);

        boolean result = this.validateSmsTO.validate(smsRequestTO);
        assertFalse(result);
        assertEquals(ResponseMessagesKeys.INVALID_TO, validateSmsTO.getErrorKey());
    }

    @Test
    public void validateToMax() throws Exception {
        String from = "919900000000";
        String text = "Valid message";
        String to = "12345678 12345678";

        SmsRequestTO smsRequestTO = getSmsRequestTO(from, text, to);

        boolean result = this.validateSmsTO.validate(smsRequestTO);
        assertFalse(result);
        assertEquals(ResponseMessagesKeys.INVALID_TO, validateSmsTO.getErrorKey());
    }

    @Test
    public void validateTextMin() throws Exception {
        String from = "919900000000";
        String text = "";
        String to = "919911111111";

        SmsRequestTO smsRequestTO = getSmsRequestTO(from, text, to);

        boolean result = this.validateSmsTO.validate(smsRequestTO);
        assertFalse(result);
        assertEquals(ResponseMessagesKeys.INVALID_TEXT, validateSmsTO.getErrorKey());
    }

    @Test
    public void validateTextMax() throws Exception {
        String from = "919900000000";
        String text = "0123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 ";
        String to = "919911111111";

        SmsRequestTO smsRequestTO = getSmsRequestTO(from, text, to);

        boolean result = this.validateSmsTO.validate(smsRequestTO);
        assertFalse(result);
        assertEquals(ResponseMessagesKeys.INVALID_TEXT, validateSmsTO.getErrorKey());
    }

    private SmsRequestTO getSmsRequestTO(String from, String text, String to) {
        SmsRequestTO smsRequestTO = new SmsRequestTO();

        smsRequestTO.setFrom(from);
        smsRequestTO.setText(text);
        smsRequestTO.setTo(to);

        return smsRequestTO;
    }

}