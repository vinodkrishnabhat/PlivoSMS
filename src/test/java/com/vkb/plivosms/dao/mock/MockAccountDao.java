package com.vkb.plivosms.dao.mock;

import com.vkb.plivosms.dao.IAccountDao;
import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.AccountEntity;

import java.util.LinkedList;
import java.util.List;

public class MockAccountDao implements IAccountDao{
    private static List<AccountEntity> data;

    private static MockAccountDao ourInstance = new MockAccountDao();

    public static MockAccountDao getInstance() {
        return ourInstance;
    }

    private MockAccountDao() {
        data = new LinkedList<AccountEntity>();
        AccountEntity account = new AccountEntity(1, "20S0KPNOIM", "plivo1" );
        data.add(account);
    }

    public AccountEntity searchAccountEntity(String username, String authId) throws PlivoException {
        for(AccountEntity entity: data) {
            if(entity.getUserName().equals(username) && entity.getAuthId().equals(authId))
            {
                return entity;
            }
        }

        return null;
    }
}
