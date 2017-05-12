package com.cn.service;

import com.cn.util.Config;
import com.cn.util.File.DownloadFile;
import com.cn.util.Message;
import com.cn.util.Out;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by jecyhw on 2015/6/9.
 */
public class DownloadFileService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("file");
        Message message = new Message();
        if (fileName != null) {
            File file = new File(fileName);
            if (!file.exists()) {
                fileName = fileName.replaceFirst("^/+", "");
                file = new File(Config.getZipFileDir() + fileName);
            }
            if (file.exists() && file.isFile()) {
                new DownloadFile().work(response, file);
                String isDeletedStr = request.getParameter("isDeleted");
                if (isDeletedStr != null && isDeletedStr.equals("1")) {
                    FileUtils.deleteQuietly(file);
                }
            } else {
                message.setStatus(1);
                message.setResult("下载文件不存在");
            }
        } else {
            message.setStatus(2);
            message.setResult("缺少file参数");
        }
        if (message.getResult() != null) {
            Out out = new Out(response);
            out.printJson(message);
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
