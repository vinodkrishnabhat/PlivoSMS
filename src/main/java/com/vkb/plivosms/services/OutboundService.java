package com.vkb.plivosms.services;

import com.vkb.plivosms.constants.ConfigKeys;
import com.vkb.plivosms.constants.ResponseMessagesKeys;
import com.vkb.plivosms.dao.DaoFactory;
import com.vkb.plivosms.dao.IBlockedPairDao;
import com.vkb.plivosms.dao.IPhoneNumberDao;
import com.vkb.plivosms.dao.ISmsCountDao;
import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.exception.PlivoForbiddenException;
import com.vkb.plivosms.objects.*;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

@Path("")
public class OutboundService extends SMSService {
    private static final Logger logger = Logger.getLogger(OutboundService.class);
    private static final int THROTTLE_LIMIT = new Integer(ResourceBundle.getBundle("config").getString(ConfigKeys.THROTTLE_LIMIT));
    private static final String RESET = "RESET";

    public OutboundService() {
        this.phoneNumberDao = DaoFactory.getPhoneNumberDao();
        this.blockedPairDao = DaoFactory.getBlockedPairDao();
        this.smsCountDao = DaoFactory.getSmsCountDao();
    }

    private IPhoneNumberDao phoneNumberDao;
    private IBlockedPairDao blockedPairDao;
    private ISmsCountDao smsCountDao;

    void setPhoneNumberDao(IPhoneNumberDao phoneNumberDao) {
        this.phoneNumberDao = phoneNumberDao;
    }
    void setBlockedPairDao(IBlockedPairDao blockedPairDao) {
        this.blockedPairDao = blockedPairDao;
    }
    void setSmsCountDao(ISmsCountDao smsCountDao) {
        this.smsCountDao = smsCountDao;
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

            SmsResponseTO smsResponseTO = doAllOutboundValidations(smsRequestTO, authEntity.getId());
            if(smsResponseTO != null) {
                return smsResponseTO;
            }

            smsResponseTO = doProcessing(smsRequestTO);
            if(smsResponseTO != null) {
                return smsResponseTO;
            }

            return getResponse(ResponseMessagesKeys.OUTBOUND_SMS_OK, ResponseMessagesKeys.ERROR_BLANK);
        }
        catch (ForbiddenException fe) {
            logger.error(fe.getMessage());
            throw fe;
        }
        catch (Exception e) {
            return getResponse(ResponseMessagesKeys.MESSAGE_BLANK, ResponseMessagesKeys.UNKNOWN_FAILURE);
        }
    }

    @POST
    @Path("adminReset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SmsResponseTO resetCounter(SmsRequestTO smsRequestTO, @HeaderParam("authorization") String authString) {

        try {
            AccountEntity authEntity = isUserAuthenticated(authString);
            if(authEntity == null) {
                throw new PlivoForbiddenException(PlivoForbiddenException.UNABLE_TO_GET_AUTH_ENTITY);
            }

            String txt = smsRequestTO.getText();
            if(RESET.equals(txt)) {
                SMSCountEntity countEntity = this.smsCountDao.getSmsCountEntity(smsRequestTO.getFrom());
                if(countEntity != null) {
                    Timestamp newTime = new Timestamp(System.currentTimeMillis());
                    this.smsCountDao.resetCounter(newTime, countEntity.getId(), countEntity.getFromNumber());
                }
            }

            return getResponse(ResponseMessagesKeys.OUTBOUND_SMS_OK, ResponseMessagesKeys.ERROR_BLANK);
        }
        catch (ForbiddenException fe) {
            logger.error(fe.getMessage());
            throw fe;
        }
        catch (Exception e) {
            return getResponse(ResponseMessagesKeys.MESSAGE_BLANK, ResponseMessagesKeys.UNKNOWN_FAILURE);
        }
    }

    private SmsResponseTO doAllOutboundValidations(SmsRequestTO smsRequestTO, Integer authAccountId) throws PlivoException{
        SmsResponseTO response = validateParams(smsRequestTO);
        if (response != null) {
            return response;
        }

        List<PhoneNumberEntity> list = this.phoneNumberDao.searchPhoneNumberEntityByNumber(smsRequestTO.getFrom(), authAccountId);
        if (list == null || list.size() == 0) {
            return getResponse(ResponseMessagesKeys.MESSAGE_BLANK, ResponseMessagesKeys.FROM_PARAMETER_NOT_FOUND);
        }

        List<BlockedPairEntity> blockedPairList = this.blockedPairDao.getBlockedPair(smsRequestTO.getFrom(), smsRequestTO.getTo());
        if(blockedPairList != null && blockedPairList.size() > 0) {
            response = getResponse(ResponseMessagesKeys.MESSAGE_BLANK, ResponseMessagesKeys.BLOCKED_BY_STOP);
            response.setError(response.getError().replace("<from>", smsRequestTO.getFrom()));
            response.setError(response.getError().replace("<to>", smsRequestTO.getTo()));
            return response;
        }

        return null;
    }

    private SmsResponseTO doProcessing(SmsRequestTO smsRequestTO) throws PlivoException {
        SMSCountEntity countEntity = this.smsCountDao.getSmsCountEntity(smsRequestTO.getFrom());

        if(countEntity == null) {
            //First time user
            this.smsCountDao.insertSmsCountEntity(smsRequestTO.getFrom());
        } else {
            Timestamp time = countEntity.getStartTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            cal.add(Calendar.DAY_OF_WEEK, 1);
            time.setTime(cal.getTime().getTime());

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            if(time.after(currentTime)) {
                //User has accessed in counter window
                if(countEntity.getCounter() >= THROTTLE_LIMIT) {
                    SmsResponseTO response = getResponse(ResponseMessagesKeys.MESSAGE_BLANK, ResponseMessagesKeys.LIMIT_REACHED);
                    response.setError(response.getError().replace("<from>", smsRequestTO.getFrom()));
                    return response;
                }

                //Can catch PlivoException in case we want to strictly return back error message for limit. (There is a check in SQL in the dao)
                this.smsCountDao.incrementCounter(countEntity.getId(), countEntity.getFromNumber());
            }
            else {
                //User has NOT accessed in counter window
                Calendar newCal = Calendar.getInstance();
                newCal.setTime(currentTime);
                newCal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
                newCal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
                newCal.set(Calendar.SECOND, cal.get(Calendar.SECOND));
                newCal.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));

                if(newCal.getTime().after(currentTime)) {
                    newCal.add(Calendar.DAY_OF_WEEK, -1);
                }
                Timestamp newTime = new Timestamp(System.currentTimeMillis());
                newTime.setTime(newCal.getTime().getTime());

                this.smsCountDao.resetCounter(newTime, countEntity.getId(), countEntity.getFromNumber());
            }
        }

        return null;
    }
}
