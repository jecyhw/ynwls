package com.cn.action;

import com.cn.test.TestOutput;
import com.cn.util.Config;
import com.cn.util.File.FileUtil;
import com.cn.util.File.JFile;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by jecyhw on 2016/2/18.
 */
public class AutoRunServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void init() throws ServletException {
        super.init();
        TestOutput.println("AutoRunServlet start");
        EnumerationKMZFiles(new File(Config.getUploadDir()));
        TestOutput.println("AutoRunServlet end");
    }

    protected void EnumerationKMZFiles(File dir) {
        try {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    if (file.getName().contains(".kmz")) {
                        JFile.addUploadFilePath(file.getAbsolutePath());
                    }
                } else {
                    EnumerationKMZFiles(file);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        }
    }
}
