package com.vkb.plivosms.dao;

import com.vkb.plivosms.dao.mock.MockAccountDao;
import com.vkb.plivosms.dao.mock.MockBlockedPairDao;
import com.vkb.plivosms.dao.mock.MockPhoneNumberDao;
import com.vkb.plivosms.dao.mock.MockSmsCountDao;

public class DaoFactoryTest {
    public static IPhoneNumberDao getPhoneNumberDao() { return MockPhoneNumberDao.getInstance();}
    public static IBlockedPairDao getBlockedPairDao() { return MockBlockedPairDao.getInstance();}
    public static IAccountDao getAccountDao() { return MockAccountDao.getInstance();}
    public static ISmsCountDao getSmsCountDao() { return MockSmsCountDao.getInstance();}

}