package com.cn.action;

import com.cn.bean.SjyfiUserEntity;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by jecyhw on 2014/10/20.
 */
public class UserLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取
        List<Object> values = new ArrayList<Object>();
        values.add(request.getParameter("account"));

        AEntityDao dao = new SjyfiUserDao();
        String sql = "select uid, name, role from " + TableName.getUser() + " where account = ?";
        SjyfiUserEntity entity = (SjyfiUserEntity) DBUtil.query(dao, sql, values);

        Message message = new Message();
        message.setStatus(1);
        if (entity == null) {
            message.setResult("用户名不存在");
        } else {
            values.add(request.getParameter("pwd"));
            sql = "select uid, name, role from " + TableName.getUser() + " where account = ? and password = ?";
            entity = (SjyfiUserEntity) DBUtil.query(dao, sql, values);
            if (entity == null)
            {
                message.setResult("密码不正确");
            } else {
                message.setStatus(0);
                message.setResult(entity);
                HttpSession session = request.getSession();
                session.setAttribute("userName", entity.getName());
                session.setAttribute("role", entity.getRole());
            }
        }
        Out out = new Out(response);
        out.printJson(message);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
