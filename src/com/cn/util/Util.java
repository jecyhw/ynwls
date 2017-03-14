package com.cn.util;

/**
 * Created by jecyhw on 2015/7/13.
 */
public class Util {
    static public Double parseDouble(String s) {
        Double result = 0.0;
        try {
            result = Double.parseDouble(s);
        } catch (Exception e) {

        }
        return result;
    }
}
