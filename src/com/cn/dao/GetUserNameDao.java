package com.cn.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by SNNU on 2015/5/11.
 */
public class GetUserNameDao extends AEntityDao {
    @Override
    public Object getEntity(ResultSet set) throws SQLException {
        return set.getString("name");
    }
}
