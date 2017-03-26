package com.vkb.plivosms.dao.mock;

import com.vkb.plivosms.dao.ISmsCountDao;
import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.SMSCountEntity;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class MockSmsCountDao implements ISmsCountDao {
    private static MockSmsCountDao ourInstance = new MockSmsCountDao();

    public static MockSmsCountDao getInstance() {
        return ourInstance;
    }

    private static List<SMSCountEntity> data;

    private MockSmsCountDao() {
        data = new LinkedList<SMSCountEntity>();

        int ctr = 1;
        SMSCountEntity entity = new SMSCountEntity(ctr, "returninguser1", new Timestamp(System.currentTimeMillis() - 1000), 5);
        data.add(entity);

        ctr++;
        entity = new SMSCountEntity(ctr, "returninguser2", new Timestamp(System.currentTimeMillis() - 1000), 50);
        data.add(entity);

        ctr++;
        entity = new SMSCountEntity(ctr, "returninguser3", new Timestamp(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000 - 10000), 50);
        data.add(entity);

        ctr++;
        entity = new SMSCountEntity(ctr, "returninguser4", new Timestamp(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000 + 10000), 50);
        data.add(entity);
    }

    public SMSCountEntity getSmsCountEntity(String number) throws PlivoException {
        for(SMSCountEntity entity: data) {
            if(entity.getFromNumber().equals(number)) {
                return entity;
            }
        }

        return null;
    }

    public void insertSmsCountEntity(String fromNumber) throws PlivoException {

    }

    public void incrementCounter(Integer id, String fromNumber) throws PlivoException {

    }

    public void resetCounter(Timestamp newStartTime, Integer id, String fromNumber) throws PlivoException {

    }
}
