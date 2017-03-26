package com.vkb.plivosms.dao.mock;

import com.vkb.plivosms.dao.IPhoneNumberDao;
import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.PhoneNumberEntity;

import java.util.LinkedList;
import java.util.List;

public class MockPhoneNumberDao implements IPhoneNumberDao {
    private static List<PhoneNumberEntity> data;

    private static MockPhoneNumberDao ourInstance = new MockPhoneNumberDao();

    public static MockPhoneNumberDao getInstance() {
        return ourInstance;
    }

    private MockPhoneNumberDao() {
        data = new LinkedList<PhoneNumberEntity>();

        int ctr = 1;
        PhoneNumberEntity entity = new PhoneNumberEntity(ctr, "4924195509196", 1);
        data.add(entity);

        ctr++;
        entity = new PhoneNumberEntity(ctr, "4924195509198", 1);
        data.add(entity);

        ctr++;
        entity = new PhoneNumberEntity(ctr, "firsttimeuser", 1);
        data.add(entity);

        ctr++;
        entity = new PhoneNumberEntity(ctr, "returninguser1", 1);
        data.add(entity);

        ctr++;
        entity = new PhoneNumberEntity(ctr, "returninguser2", 1);
        data.add(entity);

        ctr++;
        entity = new PhoneNumberEntity(ctr, "returninguser3", 1);
        data.add(entity);

        ctr++;
        entity = new PhoneNumberEntity(ctr, "returninguser4", 1);
        data.add(entity);
    }


    public List<PhoneNumberEntity> searchPhoneNumberEntityByNumber(String number, Integer id) throws PlivoException {
        List<PhoneNumberEntity> retList = new LinkedList<PhoneNumberEntity>();

        for(PhoneNumberEntity phoneNumberEntity: data) {
            if(phoneNumberEntity.getNumber().equals(number) && phoneNumberEntity.getAccountId().equals(id)) {
                retList.add(phoneNumberEntity);
            }
        }

        return retList;
    }
}
