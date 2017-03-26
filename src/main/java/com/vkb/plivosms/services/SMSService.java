package com.vkb.plivosms.services;

import com.vkb.plivosms.constants.ResponseMessagesKeys;
import com.vkb.plivosms.dao.DaoFactory;
import com.vkb.plivosms.dao.IAccountDao;
import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.exception.PlivoForbiddenException;
import com.vkb.plivosms.objects.AccountEntity;
import com.vkb.plivosms.objects.SmsRequestTO;
import com.vkb.plivosms.objects.SmsResponseTO;
import com.vkb.plivosms.utils.Base64Util;
import com.vkb.plivosms.validators.ValidateSmsTO;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

abstract class SMSService {
    private static final Logger logger = Logger.getLogger(SMSService.class);
    private static ResourceBundle responseMessages = ResourceBundle.getBundle("responseMessages");

    SMSService() {
        this.accountDao = DaoFactory.getAccountDao();
    }

    private IAccountDao accountDao;

    void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    SmsResponseTO validateParams(SmsRequestTO smsRequestTO) throws PlivoException{
        ValidateSmsTO validator = new ValidateSmsTO();
        boolean valid = validator.validate(smsRequestTO);
        if(!valid) {
            String errorKey = validator.getErrorKey();
            return getResponse(ResponseMessagesKeys.MESSAGE_BLANK, errorKey);
        }
        return null;
    }

    AccountEntity isUserAuthenticated(String authString) throws PlivoException {
        try {
            //Format of authString expected to be "Basic Base64code"
            String[] authParts = authString.split("\\s+");
            String authMethod = authParts[0];
            String authInfo = authParts[1];

            if(! "Basic".equalsIgnoreCase(authMethod)) {
                throw new PlivoForbiddenException(PlivoForbiddenException.UNABLE_TO_DECIPHER_AUTH_METHOD);
            }

            String decodedAuth = Base64Util.decode(authInfo);

            String username = decodedAuth.split(":")[0];
            String password = decodedAuth.split(":")[1];

            return this.accountDao.searchAccountEntity(username, password);
        }
        catch (RuntimeException re) {
            logger.error(re.toString());
            throw new PlivoForbiddenException(re);
        }
    }

    SmsResponseTO getResponse(String message, String error) {
        SmsResponseTO response = new SmsResponseTO();
        response.setMessage(responseMessages.getString(message));
        response.setError(responseMessages.getString(error));
        return response;
    }
}
