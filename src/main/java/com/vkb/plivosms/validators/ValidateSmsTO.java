package com.vkb.plivosms.validators;

import com.vkb.plivosms.constants.ConfigKeys;
import com.vkb.plivosms.constants.ResponseMessagesKeys;
import com.vkb.plivosms.objects.SmsRequestTO;

import java.util.ResourceBundle;

public class ValidateSmsTO {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

    private static Integer minFrom = new Integer(resourceBundle.getString(ConfigKeys.PARAM_FROM_MIN));
    private static Integer maxFrom = new Integer(resourceBundle.getString(ConfigKeys.PARAM_FROM_MAX));
    private static Integer minTo = new Integer(resourceBundle.getString(ConfigKeys.PARAM_TO_MIN));
    private static Integer maxTo = new Integer(resourceBundle.getString(ConfigKeys.PARAM_TO_MAX));
    private static Integer minText = new Integer(resourceBundle.getString(ConfigKeys.PARAM_TEXT_MIN));
    private static Integer maxText = new Integer(resourceBundle.getString(ConfigKeys.PARAM_TEXT_MAX));

    public String getErrorKey() {
        return errorKey;
    }

    private String errorKey;

    public boolean validate(SmsRequestTO smsRequestTO) {
        if(validateFromParam(smsRequestTO.getFrom(), minFrom, maxFrom)) {
            if(validateToParam(smsRequestTO.getTo(), minTo, maxTo)) {
                if(validateTextParam(smsRequestTO.getText(), minText, maxText)) {
                    this.errorKey = "";
                    return true;
                }
            }
        }

        return false;
    }

    private boolean validateFromParam(String str, int min, int max) {
        return validateParam(str, min, max, ResponseMessagesKeys.MISSING_FROM, ResponseMessagesKeys.INVALID_FROM);
    }

    private boolean validateToParam(String str, int min, int max) {
        return validateParam(str, min, max, ResponseMessagesKeys.MISSING_TO, ResponseMessagesKeys.INVALID_TO);
    }

    private boolean validateTextParam(String str, int min, int max) {
        return validateParam(str, min, max, ResponseMessagesKeys.MISSING_TEXT, ResponseMessagesKeys.INVALID_TEXT);
    }

    private boolean validateParam(String str, int min, int max, String missingErrorKey, String invalidErrorKey) {
        if(str == null) {
            this.errorKey = missingErrorKey;
            return false;
        }

        if (str.length() < min || str.length() > max) {
            this.errorKey = invalidErrorKey;
            return false;
        }

        this.errorKey = "";
        return true;
    }
}
