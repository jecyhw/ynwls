package com.cn.bean;

/**
 * Created by jecyhw on 2017/3/23.
 */
public class PageInfo {
    private Integer page = 1;
    private Integer size = 10;

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
        return (page - 1) * size ;
    }
}
