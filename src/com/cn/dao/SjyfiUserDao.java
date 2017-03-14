package com.cn.dao;

import com.cn.bean.SjyfiUserEntity;
import com.cn.util.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jecyhw on 2014/10/20.
 */
public class SjyfiUserDao extends AEntityDao {
    @Override
    public Object getEntity(ResultSet set) throws SQLException {
        SjyfiUserEntity entity = new SjyfiUserEntity();
        entity.setName(set.getString("name"));
        entity.setRole(set.getInt("role"));
        entity.setUid(set.getInt("uid"));
        return entity;
    }
}
