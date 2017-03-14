package com.cn.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by SNNU on 2015/3/22.
 */
public class CheckFileExistDao extends AEntityDao {
    @Override
    public Object getEntity(ResultSet set) throws SQLException {
        return true;
    }
}
