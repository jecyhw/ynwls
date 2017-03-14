package com.cn.util;

/**
 * Created by jecyhw on 2014/10/20.
 */
public class SqlAssist {
    static public String addQuote(String fieldValue) {
        return "'" + fieldValue.replace("'", "\\'") + "'";
    }

    static public String addEqual(String fieldName, String fieldValue) {
        return " " + fieldName + "=" + addQuote(fieldValue) + " ";
    }

    static public String addGreat(String fieldName, String fieldValue) {
        return " " + fieldName + ">" + addQuote(fieldValue) + " ";
    }

    static public String addGreatEqual(String fieldName, String fieldValue) {
        return " " + fieldName + ">=" + addQuote(fieldValue) + " ";
    }

    static public String addLess(String fieldName, String fieldValue) {
        return " " + fieldName + "<" + addQuote(fieldValue) + " ";
    }

    static public String addLessEqual(String fieldName, String fieldValue) {
        return " " + fieldName + "<=" + addQuote(fieldValue) + " ";
    }
}
