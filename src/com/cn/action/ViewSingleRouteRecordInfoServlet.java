package com.cn.action;

import com.cn.dao.AEntityDao;
import com.cn.dao.TTracksDao;
import com.cn.util.DBUtil;
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
 * Created by acm on 11/29/14.
 */
public class ViewSingleRouteRecordInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Object> values = new ArrayList<Object>();
        values.add(request.getParameter("id"));

        AEntityDao dao = new TTracksDao();
        String sql ="select * from " + TableName.getTracks() + " where trackid = ?";

        Out out = new Out(response);
        out.printJson(DBUtil.query(dao, sql, values));
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
