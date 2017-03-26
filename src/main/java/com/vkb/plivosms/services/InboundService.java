package com.vkb.plivosms.services;

import com.vkb.plivosms.constants.ResponseMessagesKeys;
import com.vkb.plivosms.dao.DaoFactory;
import com.vkb.plivosms.dao.IBlockedPairDao;
import com.vkb.plivosms.dao.IPhoneNumberDao;
import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.exception.PlivoForbiddenException;
import com.vkb.plivosms.objects.*;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("")
public class InboundService extends SMSService {
    private static final Logger logger = Logger.getLogger(InboundService.class);

    private static final List<String> STOP_STRINGS = Arrays.asList("STOP", "STOP\n", "STOP\r", "STOP\r\n");
    private static final String RESET = "RESET";

    public InboundService() {
        this.phoneNumberDao = DaoFactory.getPhoneNumberDao();
        this.blockedPairDao = DaoFactory.getBlockedPairDao();
    }

    private IPhoneNumberDao phoneNumberDao;
    private IBlockedPairDao blockedPairDao;

    void setPhoneNumberDao(IPhoneNumberDao phoneNumberDao) {
        this.phoneNumberDao = phoneNumberDao;
    }
    void setBlockedPairDao(IBlockedPairDao blockedPairDao) {
        this.blockedPairDao = blockedPairDao;
    }

    @POST
    @Path("sms")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SmsResponseTO handleSMS(SmsRequestTO smsRequestTO, @HeaderParam("authorization") String authString) {
        try {
            AccountEntity authEntity = isUserAuthenticated(authString);
            if(authEntity == null) {
                throw new PlivoForbiddenException(PlivoForbiddenException.UNABLE_TO_GET_AUTH_ENTITY);
            }

            SmsResponseTO smsResponseTO = doAllInboundValidations(smsRequestTO, authEntity.getId());
            if(smsResponseTO != null) {
                return smsResponseTO;
            }

            doProcessing(smsRequestTO);
            return getResponse(ResponseMessagesKeys.INBOUND_SMS_OK, ResponseMessagesKeys.ERROR_BLANK);
        }
        catch (ForbiddenException fe) {
            logger.error(fe.getMessage());
            throw fe;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return getResponse(ResponseMessagesKeys.MESSAGE_BLANK, ResponseMessagesKeys.UNKNOWN_FAILURE);
        }
    }

    @POST
    @Path("adminReset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SmsResponseTO resetBlockedPair(SmsRequestTO smsRequestTO, @HeaderParam("authorization") String authString) {
        try {
            AccountEntity authEntity = isUserAuthenticated(authString);
            if(authEntity == null) {
                throw new PlivoForbiddenException(PlivoForbiddenException.UNABLE_TO_GET_AUTH_ENTITY);
            }

            String txt = smsRequestTO.getText();
            if(RESET.equals(txt)) {
                this.blockedPairDao.resetBlockedPair(smsRequestTO.getFrom(), smsRequestTO.getTo());
            }

            return getResponse(ResponseMessagesKeys.INBOUND_SMS_OK, ResponseMessagesKeys.ERROR_BLANK);
        }
        catch (ForbiddenException fe) {
            logger.error(fe.getMessage());
            throw fe;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return getResponse(ResponseMessagesKeys.MESSAGE_BLANK, ResponseMessagesKeys.UNKNOWN_FAILURE);
        }
    }

    private void doProcessing(SmsRequestTO smsRequestTO) throws PlivoException{
        String txt = smsRequestTO.getText();

        if(STOP_STRINGS.contains(txt)) {
            BlockedPairEntity blockedPairEntity = new BlockedPairEntity(null, smsRequestTO.getFrom(), smsRequestTO.getTo(), System.currentTimeMillis());
            this.blockedPairDao.setBlockedPair(blockedPairEntity);
        }
    }

    private SmsResponseTO doAllInboundValidations(SmsRequestTO smsRequestTO, Integer authAccountId) throws PlivoException{
        SmsResponseTO response = validateParams(smsRequestTO);
        if (response != null) {
            return response;
        }

        List<PhoneNumberEntity> list = this.phoneNumberDao.searchPhoneNumberEntityByNumber(smsRequestTO.getTo(), authAccountId);
        if (list == null || list.size() == 0) {
            return getResponse(ResponseMessagesKeys.MESSAGE_BLANK, ResponseMessagesKeys.TO_PARAMETER_NOT_FOUND);
        }

        return null;
    }
}
