package com.cn.service;

import com.cn.bean.ConditionEntity;
import com.cn.dao.TTracksDao;
import com.cn.util.DBUtil;
import com.cn.util.Json;
import com.cn.util.Out;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jecyhw on 2015/6/6.
 */
public class QueryRecordService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List result = new ArrayList();
        ConditionEntity conditionEntity = new ConditionEntity();
        String data = request.getParameter("data");
        if (data != null) {
            conditionEntity = (ConditionEntity) Json.read(data, ConditionEntity.class);
        } else {
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
        if (!conditionEntity.isEmpty()) {
            result = DBUtil.queryMulti(new TTracksDao(), conditionEntity.getSql(), conditionEntity.getSqlValues());
        }
        Out out = new Out(response);
        out.printJson(result);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
