package com.cn.service;

import com.cn.dao.TrtGpsPointDao;
import com.cn.util.DBUtil;
import com.cn.util.Message;
import com.cn.util.Out;
import com.cn.util.TableName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by SNNU on 2015/5/6.
 */
public class UserRealTimePositionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Message message = new Message();
        String uid = request.getParameter("uid");
        TrtGpsPointDao dao = new TrtGpsPointDao();
        String sql = "select * from " + TableName.gettRtGpsPointNew();//默认获取全部用户的更新信息
        List uidList = new ArrayList();
        if (uid != null) {

            if (uid.matches("(?:[\\d]+,)*\\d+")) {
                uidList = Arrays.asList(uid.split(","));
                if (uidList.size() > 0) {
                    sql += " where uid in (" + uid.replaceAll("[^,]+", "?") + ")";
                }
            } else {
                message.setStatus(1);
                message.setResult("uid必须为整数值,多个值用,分隔)");
            }
        }
        else {
            String name = request.getParameter("name");
            if (name != null) {
                sql += " where name like ?";
                uidList.add("%" + name + "%");
            }
        }

        if (message.getResult() == null) {
            message.setResult(DBUtil.queryMulti(dao, sql, uidList));
            message.setStatus(0);
        }

        Out out = new Out(response);
        out.printJson(message);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
