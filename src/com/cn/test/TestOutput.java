package com.cn.test;

import java.io.*;
import java.util.Date;

/**
 * Created by jecyhw on 2014/10/16.
 */
public class TestOutput {
    static private String outPutFileName = System.getProperty("user.dir") + "/log.txt";
    static private Boolean isOutPutFile = false;
    static private String lineSeparator = System.getProperty("line.separator");
    static {
        File file = new File(outPutFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                TestOutput.println(e.getMessage());
            }
        }
    }
    static public void println(Object object)
    {
        if (isOutPutFile) {
            try {
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outPutFileName, true), "utf-8");
                writer.write(new Date().toString() + "::" + object + lineSeparator);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(object);
        }
    }
}
