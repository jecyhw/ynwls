package com.cn.util;

/**
 * Created by jecyhw on 2015/6/16.
 */
public class Message {
    Integer status;
    Object result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Status{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }
}
