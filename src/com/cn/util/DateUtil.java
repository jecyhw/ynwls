package com.cn.util;

import com.cn.test.TestOutput;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by SNNU on 2014/11/15.
 */
public class DateUtil {
    static public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static public Date string2Date(String dateString) {
        Date date = null;
        try {
             date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
        return date;
    }

    static public Date string2Date(String dateString, SimpleDateFormat dateFormat) {
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
        return date;
    }

    /**
     *
     * @param dateString
     * @return 转换异常返回当前时间
     */
    static public Timestamp string2Timestamp(String dateString) {
        dateString = dateString.trim();
        if (dateString.indexOf(':') < 0) {
            dateString += " 00:00:00";
        }
        Timestamp result = new Timestamp(System.currentTimeMillis());
        try {
            result = Timestamp.valueOf(dateString);
        } catch (Exception e) {

        }
        return result;
    }

    static public String date2String(Date date){
        return dateFormat.format(date);
    }

    static public String date2String(Date date, SimpleDateFormat dateFormat) {
        return dateFormat.format(date);
    }
}
