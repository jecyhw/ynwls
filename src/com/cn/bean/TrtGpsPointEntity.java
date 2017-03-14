package com.cn.bean;

import com.cn.util.JsonTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.sql.Timestamp;

/**
 * Created by SNNU on 2015/5/5.
 */
public class TrtGpsPointEntity {
    private Integer uid;

    //@JsonSerialize(using = JsonTimestampSerializer.class)
    private Timestamp time;
    private String name;
    private Double longitude;
    private Double latitude;
    private Double altitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "TrtGpsPointEntity{" +
                "uid=" + uid +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                ", time=" + time +
                ", name='" + name + '\'' +
                '}';
    }
}
