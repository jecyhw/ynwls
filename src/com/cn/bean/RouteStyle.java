package com.cn.bean;

/**
 * Created by SNNU on 2015/3/19.
 */
public class RouteStyle {
    String color;
    String width;
    String id;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color.length() > 6) {
            color = color.substring(0, 6);
        }
        this.color = "#" + color;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
