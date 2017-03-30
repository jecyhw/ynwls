package com.cn.service;

import com.cn.bean.ConditionEntity;
import com.cn.bean.ConditionPageEntity;
import com.cn.bean.PageInfo;
import com.cn.dao.AEntityDao;
import com.cn.dao.TTracksDao;
import com.cn.util.DBUtil;
import com.cn.util.Json;
import com.cn.util.Out;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jecyhw on 2017/3/30.
 */
public class QueryRecordPageService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConditionPageEntity conditionEntity = new ConditionPageEntity();
        String data = request.getParameter("data");
        if (data != null) {
            conditionEntity = (ConditionPageEntity) Json.read(data, ConditionPageEntity.class);
        } else {
            String page = request.getParameter("page");
            String size = request.getParameter("size");
            if (page != null) {
                conditionEntity.setPage(Integer.valueOf(page));
            }
            if ( size != null) {
                conditionEntity.setSize(Integer.valueOf(size));
            }
            conditionEntity.setRecorder(request.getParameter("author"));
            conditionEntity.setAnnotation(request.getParameter("annotation"));
            conditionEntity.setKeysiteslist(request.getParameter("keysiteslist"));
            conditionEntity.setName(request.getParameter("name"));

            String temp = request.getParameter("starttime");//开始时间
            if (temp != null) {
                try {
                    conditionEntity.setStartTime(new Timestamp(new SimpleDateFormat("yyyyMMddHHmmss").parse(temp).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            temp = request.getParameter("endtime");//结束时间
            if (temp != null) {
                try {
                    conditionEntity.setEndTime(new Timestamp(new SimpleDateFormat("yyyyMMddHHmmss").parse(temp).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            temp = request.getParameter("latitude");//纬度
            if (temp != null) {
                String[] lats = temp.split("-");
                try {
                    conditionEntity.setTop(Double.parseDouble(lats[0]));
                } catch (NumberFormatException e) {

                }
                try {
                    conditionEntity.setBottom(Double.parseDouble(lats[1]));
                } catch (NumberFormatException e) {

                } catch (IndexOutOfBoundsException e) {

                }
            }
            temp = request.getParameter("longitude");//经度
            if (temp != null) {
                String[] longs = temp.split("-");
                try {
                    conditionEntity.setLeft(Double.parseDouble(longs[0]));
                } catch (NumberFormatException e) {

                }
                try {
                    conditionEntity.setRight(Double.parseDouble(longs[1]));
                } catch (NumberFormatException e) {

                } catch (IndexOutOfBoundsException e) {

                }
            }
        }
        //if (!conditionEntity.isEmpty()) {
        String countSql = conditionEntity.getCountSql();
        List args = conditionEntity.getSqlValues();
        Integer count = (Integer) DBUtil.query(new AEntityDao() {
            @Override
            public Object getEntity(ResultSet set) throws SQLException {
                return set.getInt("count");
            }
        }, countSql, args.subList(0, args.size() - 2));

        List list = DBUtil.queryMulti(new TTracksDao(), conditionEntity.getSql(), args);
        Map<String, Object> result = new HashMap<>();
        result.put("total", count);
        result.put("data", list);
        Out out = new Out(response);
        out.printJson(result);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
