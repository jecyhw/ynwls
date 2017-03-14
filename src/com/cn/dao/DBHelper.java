package com.cn.dao;

import com.cn.test.TestOutput;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SNNU on 2014/11/16.
 */
public class DBHelper {
    static public String getInsertSql(String tableName, Object src) {
        StringBuilder stringBuilder = new StringBuilder();
        String values = "";
        stringBuilder
                .append("insert into ")
                .append(tableName)
                .append(" (");
        Field[] fields = getObjectFields(src);
        for (Field field : fields) {
            Object fieldValue = getFieldVale(src, field);
            if (fieldValue != null) {
                stringBuilder.append(field.getName()).append(",");
                values += "?,";
            }
        }
        stringBuilder
                .deleteCharAt(stringBuilder.length() - 1)
                .append(") values (")
                .append(values)
                .deleteCharAt(stringBuilder.length() - 1)
                .append(");");
        return stringBuilder.toString();
    }

    static public List getSqlValues(Object src) {
        List sqlValues = new ArrayList();
        Field[] fields = getObjectFields(src);
        for (Field field : fields) {
            Object fieldValue = getFieldVale(src, field);
            if (fieldValue != null) {
                sqlValues.add(fieldValue);
            }
        }
        return sqlValues;
    }

    static protected Object getFieldVale(Object src, Field field) {
        field.setAccessible(true);
        Object fieldValue = null;
        try {
            fieldValue = field.get(src);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        }
        return fieldValue;
    }

    static protected Field[] getObjectFields(Object src) {
        Class cls = src.getClass();
        return cls.getDeclaredFields();
    }
}
