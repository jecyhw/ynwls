package com.cn.dao;

import com.cn.bean.SjyfiUserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jecyhw on 2015/6/11.
 */
public class CheckUserDao extends AEntityDao {
    @Override
    public Object getEntity(ResultSet set) throws SQLException {
        SjyfiUserEntity entity = new SjyfiUserEntity();
        entity.setUid(set.getInt("uid"));
        entity.setAccount(set.getString("account"));
        entity.setPassword(set.getString("password"));
        entity.setName(set.getString("name"));
        entity.setGender(set.getByte("gender"));
        entity.setBirthday(set.getTimestamp("birthday"));
        entity.setOrganization(set.getString("organization"));
        entity.setCountry(set.getString("country"));
        entity.setProvince(set.getString("province"));
        entity.setCity(set.getString("city"));
        entity.setCounty(set.getString("county"));
        entity.setTownship(set.getString("township"));
        entity.setAdd_time(set.getTimestamp("add_time"));
        entity.setRole(set.getInt("role"));
        return entity;
    }
}
