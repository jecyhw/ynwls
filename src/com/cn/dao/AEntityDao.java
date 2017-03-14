package com.cn.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SNNU on 2014/11/16.
 */

/**
 *采用preparedstatement预编译执行语句
 */
public abstract class AEntityDao{
    /**
     * 得到一个javabean对象，不判断当前set是否有记录,供getEntityList方法调用，所以无需判断
     * @param set
     * @return
     */
    public abstract Object getEntity(ResultSet set) throws SQLException;

    /**
     * 根据set得到一个javabean对象列表，
     * @param set
     * @return
     */
    public List<Object> getEntityList(ResultSet set) throws SQLException {
        List<Object> result = new ArrayList<Object>();
        while (set.next()) {
            result.add(getEntity(set));
        }
        return result;
    }


}
