package com.cn.bean;

import java.util.List;

/**
 * Created by SNNU on 2015/3/20.
 */
public class KeyPointPlaceMark {
    private String name;//两者都有
    private List desc;//关键点的描述
    private TTracksPointsEntity coordinate;//关键点的坐标

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TTracksPointsEntity getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(TTracksPointsEntity coordinate) {
        this.coordinate = coordinate;
    }

    public List getDesc() {
        return desc;
    }

    public void setDesc(List desc) {
        this.desc = desc;
    }

    public void merge(KeyPointPlaceMark placeMark) {
        name += ", " + placeMark.getName();
        desc.addAll(placeMark.getDesc());
    }

    @Override
    public String toString() {
        return "KeyPointPlaceMark{" +
                "name='" + name + '\'' +
                ", desc=" + desc +
                ", coordinate=" + coordinate +
                '}';
    }
}
