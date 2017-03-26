package com.vkb.plivosms.dao;

import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.SMSCountEntity;

import java.sql.Timestamp;

public interface ISmsCountDao {
    SMSCountEntity getSmsCountEntity(String number) throws PlivoException;
    void insertSmsCountEntity(String fromNumber) throws PlivoException;
    void incrementCounter(Integer id, String fromNumber) throws PlivoException;
    void resetCounter(Timestamp newStartTime, Integer id, String fromNumber) throws PlivoException;
}
