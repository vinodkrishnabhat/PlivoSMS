package com.vkb.plivosms.dao;

import com.vkb.plivosms.dao.postgres.PostgresAccountDao;
import com.vkb.plivosms.dao.postgres.PostgresBlockedPairDao;
import com.vkb.plivosms.dao.postgres.PostgresPhoneNumberDao;
import com.vkb.plivosms.dao.postgres.PostgresSmsCountDao;

public class DaoFactory {
    public static IPhoneNumberDao getPhoneNumberDao() { return PostgresPhoneNumberDao.getInstance();}
    public static IBlockedPairDao getBlockedPairDao() { return PostgresBlockedPairDao.getInstance();}
    public static IAccountDao getAccountDao() { return PostgresAccountDao.getInstance();}
    public static ISmsCountDao getSmsCountDao() { return PostgresSmsCountDao.getInstance();}
}