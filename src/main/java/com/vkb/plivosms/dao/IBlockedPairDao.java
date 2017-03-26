package com.vkb.plivosms.dao;

import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.BlockedPairEntity;

import java.util.List;

public interface IBlockedPairDao {
    void setBlockedPair(BlockedPairEntity blockedPairEntity) throws PlivoException;
    List<BlockedPairEntity> getBlockedPair(String from, String to) throws PlivoException;
    void resetBlockedPair(String from, String to) throws PlivoException;
}
