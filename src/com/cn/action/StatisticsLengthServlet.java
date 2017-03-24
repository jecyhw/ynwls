package com.cn.action;

import com.cn.bean.StatisticData;
import com.cn.dao.AEntityDao;
import com.cn.util.DBUtil;
import com.cn.util.DateUtil;
import com.cn.util.Out;
import com.cn.util.TableName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jecyhw on 2017/3/24.
 */
public class StatisticsLengthServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String author = request.getParameter("author");

        List<Object> args = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        builder.append("select starttime, length from ").append(TableName.getTracks());
        builder.append(" where author = ? and  length > 0 ");
        args.add(author);

        if (startTime != null && !startTime.isEmpty()) {
            builder.append(" and starttime >= ? ");
            args.add(startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            builder.append(" and endtime <= ? ");
            args.add(endTime);
        }
        builder.append(" order by starttime");

        AEntityDao entityDao = new AEntityDao() {
            @Override
            public StatisticData getEntity(ResultSet set) throws SQLException {
                StatisticData data = new StatisticData();
                data.setName(DateUtil.date2String(set.getDate("starttime"), new SimpleDateFormat("yyyy-MM-dd")));
                data.setValue(set.getDouble("length"));
                return data;
            }
        };

        List<StatisticData> statisticDatas = DBUtil.queryMulti(entityDao, builder.toString(), args);
        Out out = new Out(response);
        out.printJson(statisticDatas);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
