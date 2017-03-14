package com.cn.service;

import com.cn.dao.AEntityDao;
import com.cn.dao.SjyfiUserDao;
import com.cn.util.DBUtil;
import com.cn.util.Message;
import com.cn.util.Out;
import com.cn.util.TableName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by jecyhw on 2015/6/9.
 */
public class CheckUserService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List values = new ArrayList();
        values.add(request.getParameter("account"));

        AEntityDao dao = new SjyfiUserDao();
        String sql = "select uid, name, role from " + TableName.getUser() + " where account = ?";

        Object entity = DBUtil.query(dao, sql, values);

        Message message = new Message();
        if (entity == null) {
            message.setStatus(0);
        } else {
            message.setStatus(1);
        }
        Out out = new Out(response);
        out.printJson(message);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
