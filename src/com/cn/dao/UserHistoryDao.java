package com.cn.dao;

import com.cn.bean.TrtGpsPointEntity;
import com.cn.bean.UserHistoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by SNNU on 2015/5/12.
 */
public class UserHistoryDao extends AEntityDao {
    Map<Integer, UserHistoryEntity> historyMap = new Hashtable<Integer, UserHistoryEntity>();
    @Override
    public Object getEntity(ResultSet set) throws SQLException {
        TrtGpsPointEntity entity = new TrtGpsPointEntity();
        int uid = set.getInt("uid");
        entity.setLongitude(set.getDouble("longitude"));
        entity.setLatitude(set.getDouble("latitude"));
        entity.setAltitude(set.getDouble("altitude"));
        entity.setTime(set.getTimestamp("time"));
        if (!historyMap.containsKey(uid)) {
            UserHistoryEntity historyEntity = new UserHistoryEntity();
            historyEntity.setUid(uid);
            historyEntity.setName(set.getString("name"));
            historyMap.put(uid, historyEntity);
        }
        historyMap.get(uid).getHistory().add(entity);
        return entity;
    }

    @Override
    public List<Object> getEntityList(ResultSet set) throws SQLException {
        while (set.next()) {
            getEntity(set);
        }
        return Arrays.asList(historyMap.values().toArray());
    }
}
