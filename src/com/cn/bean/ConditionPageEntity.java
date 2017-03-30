package com.cn.bean;

import com.cn.util.TableName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jecyhw on 2014/10/27.
 */
public class ConditionPageEntity extends ConditionEntity{
    private Integer page = 1;
    private Integer size = 10;
    private String countSql;

    public String getCountSql() {
        if (countSql == null) {
            this.get();
        }
        return countSql;
    }

    @Override
    protected void get() {
        super.get();
        countSql = sb.toString().replaceFirst("(b\\.)?\\*", "count(*) as count");
        sb.append(" order by trackid desc limit ?, ?");
        sqlValues.add(this.getStart());
        sqlValues.add(this.getSize());
        sql = sb.toString();
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getStart() {
        return (this.page - 1) * size;
    }
}
