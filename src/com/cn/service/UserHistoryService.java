package com.cn.service;

import com.cn.dao.UserHistoryDao;
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
import java.util.List;

/**
 * Created by SNNU on 2015/5/6.
 */
public class UserHistoryService extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Message message = new Message();
        String name = request.getParameter("name");
        if (name != null) {
            String sql = "select " + TableName.gettRtGpsPoint() + ".*, " + TableName.getUser() + ".name from " + TableName.gettRtGpsPoint()
                    + "," + TableName.getUser() + " where " + TableName.getUser() + ".name like  ? and "
                    + TableName.gettRtGpsPoint() + ".time >= curdate() and "
                    + TableName.getUser() + ".uid=" + TableName.gettRtGpsPoint() + ".uid"
                    +" order by " + TableName.gettRtGpsPoint() + ".uid, " + TableName.gettRtGpsPoint() + ".time";
            List uidList = new ArrayList();
            uidList.add("%" + name + "%");
            message.setStatus(0);
            message.setResult(DBUtil.queryMulti(new UserHistoryDao(), sql, uidList));
        }
        else {
            message.setResult("name参数不能为空");
            message.setStatus(1);
        }
        Out out = new Out(response);
        out.printJson(message);
        out.close();
    }
}
