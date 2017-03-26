package com.vkb.plivosms.dao;

import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.AccountEntity;

public interface IAccountDao {
    AccountEntity searchAccountEntity(String username, String authId) throws PlivoException;
}
