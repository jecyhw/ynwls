package com.cn.util;

import com.cn.test.TestOutput;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by SNNU on 2014/11/4.
 */
public class Out {
    PrintWriter pw;
    public Out(HttpServletResponse response)
    {
        try {
            this.pw = response.getWriter();
        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void printScript(String result)
    {
        pw.println("<script type='text/javascript'>" + result + "</script>");
    }

    public void printJson(Object obj)
    {
       Json.write(pw, obj);
    }

    public void printText(Object obj) {
        pw.println(obj);
    }

    public void close()
    {
        pw.close();
    }

    public static void main(String[] args) {

    }
}
