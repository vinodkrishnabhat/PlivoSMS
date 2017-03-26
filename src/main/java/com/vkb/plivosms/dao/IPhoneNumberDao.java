package com.vkb.plivosms.dao;

import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.PhoneNumberEntity;

import java.util.List;

public interface IPhoneNumberDao {
    List<PhoneNumberEntity> searchPhoneNumberEntityByNumber(String number, Integer account_id) throws PlivoException;
}
