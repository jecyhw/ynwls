package com.cn.action;

import com.cn.dao.CheckFileExistDao;
import com.cn.util.DBUtil;
import com.cn.util.File.JFile;
import com.cn.util.Out;
import com.cn.util.Message;
import com.cn.util.TableName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 12/19/14.
 */
public class CheckFileExistServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Out out = new Out(response);
        Message message = new Message();
        String fileName = request.getParameter("filename");
        if (false == JFile.checkKMZFileExist(fileName.substring(0, fileName.lastIndexOf('.')))) {
            message.setStatus(0);
            message.setResult("文件不存在");
        } else {
            message.setStatus(1);
            message.setResult("文件已存在");
        }
        out.printJson(message);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
