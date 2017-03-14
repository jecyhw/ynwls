package com.cn.util;

import com.cn.test.TestOutput;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jecyhw on 2014/10/18.
 */
public class Json {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DateUtil.dateFormat);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static void write(Writer writer, Object value)
    {
        try {
            objectMapper.writeValue(writer, value);
        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void write(OutputStream outputStream, Object value) {
        try {
            objectMapper.writeValue(outputStream, value);

        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }


    public static String writeAsString(Object value) {
        String result = "[]";
        try {
            result = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object read(String jsonString, Class clsName)
    {

        Object obj = null;
        try {
            obj = objectMapper.readValue(jsonString, clsName);
        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    public static void main(String[] args) throws IOException {

    }
}
