package com.cn.bean;

import java.util.List;

/**
 * Created by SNNU on 2015/3/20.
 */
public class RoutePlaceMark {
    private String name;//两者都有
    private List<TTracksPointsEntity> route;//轨迹的坐标
    private RouteStyle routeStyle;//轨迹的样式

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RouteStyle getRouteStyle() {
        return routeStyle;
    }

    public void setRouteStyle(RouteStyle routeStyle) {
        if (routeStyle.getColor() != null)
            this.routeStyle = routeStyle;
    }

    public List<TTracksPointsEntity> getRoute() {
        return route;
    }

    public void setRoute(List<TTracksPointsEntity> route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "RoutePlaceMark{" +
                "name='" + name + '\'' +
                ", route=" + route +
                ", routeStyle=" + routeStyle +
                '}';
    }
}
