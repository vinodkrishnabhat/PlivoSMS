package com.vkb.plivosms.dao.mock;

import com.vkb.plivosms.dao.IBlockedPairDao;
import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.BlockedPairEntity;

import java.util.LinkedList;
import java.util.List;

public class MockBlockedPairDao implements IBlockedPairDao {
    private static List<BlockedPairEntity> data;

    private static MockBlockedPairDao ourInstance = new MockBlockedPairDao();

    public void setBlockedPair(BlockedPairEntity blockedPairEntity) throws PlivoException {

    }

    public List<BlockedPairEntity> getBlockedPair(String from, String to) throws PlivoException {
        List<BlockedPairEntity> retList = new LinkedList<BlockedPairEntity>();

        for(BlockedPairEntity entity: data) {
            if(entity.getFrom().equals(from) && entity.getTo().equals(to) && entity.getSetAtTime() > (System.currentTimeMillis() - 4 * 60 * 60 * 1000)) {
                retList.add(entity);
            }
        }

        return retList;
    }

    public void resetBlockedPair(String from, String to) throws PlivoException {

    }

    public static MockBlockedPairDao getInstance() {
        return ourInstance;
    }

    private MockBlockedPairDao() {
        data = new LinkedList<BlockedPairEntity>();

        BlockedPairEntity entity = new BlockedPairEntity(1, "4924195509196", "4924195509198", System.currentTimeMillis());
        data.add(entity);
    }
}
