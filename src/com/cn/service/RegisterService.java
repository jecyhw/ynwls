package com.cn.service;

import com.cn.bean.SjyfiUserEntity;
import com.cn.dao.DBHelper;
import com.cn.util.DBUtil;
import com.cn.util.Message;
import com.cn.util.Out;
import com.cn.util.TableName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by jecyhw on 2015/6/10.
 */
public class RegisterService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Message message = new Message();
        SjyfiUserEntity userEntity = new SjyfiUserEntity();
        try {
            userEntity.setAccount(request.getParameter("account"));
            userEntity.setPassword(request.getParameter("password"));
            userEntity.setName(request.getParameter("name"));
            userEntity.setGender(Byte.valueOf(request.getParameter("gender")));
            userEntity.setBirthday(Timestamp.valueOf(request.getParameter("birthday") + " 00:00:00"));
            userEntity.setOrganization(request.getParameter("organization"));
            userEntity.setProvince(request.getParameter("province"));
            userEntity.setCity(request.getParameter("city"));
            userEntity.setCounty(request.getParameter("county"));
            userEntity.setTownship(request.getParameter("township"));
            userEntity.setEmail(request.getParameter("email"));
            userEntity.setCountry("中国");
            userEntity.setRole(-1);
        } catch (Exception e) {
            message.setStatus(1);
            message.setResult("参数格式不对");
        }
        if (message.getResult() == null) {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            userEntity.setLogin_time(time);
            userEntity.setAdd_time(time);
            userEntity.setVisit_count(0);

            System.out.println(userEntity);
            int count = DBUtil.insert(DBHelper.getInsertSql(TableName.getUser(), userEntity), DBHelper.getSqlValues(userEntity));
            if (count > 0) {
                message.setStatus(0);
                message.setResult("成功");
            } else {
                message.setStatus(2);
                message.setResult("数据库语句执行出错");
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
