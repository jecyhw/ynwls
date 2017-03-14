package com.cn.bean;

/**
 * Created by jecyhw on 2014/10/12.
 */
public class TTracksPointsEntity {
    Integer trackid;
    Double longitude;
    Double latitude;
    Double altitude;

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

    public Integer getTrackid() {
        return trackid;
    }

    public void setTrackid(Integer trackid) {
        this.trackid = trackid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TTracksPointsEntity) {
            TTracksPointsEntity entity = (TTracksPointsEntity)obj;
            if (entity.altitude.equals(altitude) && entity.latitude.equals(latitude) && entity.longitude.equals(longitude)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "TTracksPointsEntity{" +
                ", trackid=" + trackid +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                '}';
    }
}
