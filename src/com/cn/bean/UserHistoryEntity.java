package com.cn.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SNNU on 2015/5/12.
 */
public class UserHistoryEntity {
    String name;
    Integer uid;
    List<TrtGpsPointEntity> history = new ArrayList<TrtGpsPointEntity>();

    public List<TrtGpsPointEntity> getHistory() {
        return history;
    }

    public void setHistory(List<TrtGpsPointEntity> history) {
        this.history = history;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
