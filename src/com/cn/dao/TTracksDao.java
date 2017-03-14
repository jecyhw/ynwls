package com.cn.dao;

import com.cn.bean.TTracksEntity;
import com.cn.util.TableName;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by SNNU on 2014/11/16.
 */
public class TTracksDao extends AEntityDao {

    @Override
    public Object getEntity(ResultSet set) throws SQLException {
        TTracksEntity entity = new TTracksEntity();
        entity.setTrackid(set.getInt("trackid"));
        entity.setPath(set.getString("path"));
        entity.setAnnotation(set.getString("annotation"));
        entity.setAuthor(set.getString("author"));
        entity.setFilesize(set.getInt("filesize"));
        entity.setEndtime(set.getTimestamp("endtime"));
        entity.setStarttime(set.getTimestamp("starttime"));
        entity.setName(set.getString("name"));
        entity.setLength(set.getDouble("length"));
        entity.setMaxaltitude(set.getDouble("maxaltitude"));
        entity.setKeysiteslist(set.getString("keysiteslist"));
        return entity;
    }
}
