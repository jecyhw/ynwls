package com.cn.service;

import com.cn.bean.SjyfiUserEntity;
import com.cn.dao.SjyfiUserDao;
import com.cn.util.*;
import com.cn.websocket.ShowUserByRealTime;
import com.cn.bean.TrtGpsPointEntity;
import com.cn.dao.DBHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by SNNU on 2015/5/5.
 */
public class TRtGPSPointService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Message message = new Message();
        TrtGpsPointEntity entity = new TrtGpsPointEntity();
        try {
            entity.setLongitude(Double.valueOf(request.getParameter("longitude")));
            entity.setLatitude(Double.valueOf(request.getParameter("latitude")));
            entity.setAltitude(Double.valueOf(request.getParameter("altitude")));
            entity.setTime(Timestamp.valueOf(request.getParameter("time")));
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.MILLISECOND, 0);
            if (entity.getTime().getTime() <= cal.getTimeInMillis()) {
                message.setResult("time日期已经过时");
            }
        } catch (Exception e) {
            if (entity.getLongitude() == null) {
                message.setResult("longitude值缺失或者格式不对(必须为数值型)");
            } else if (entity.getLatitude() == null) {
                message.setResult("latitude值缺失或者格式不对(必须为数值型)");
            } else if (entity.getAltitude() == null) {
                message.setResult("altitude值缺失或者格式不对(必须为数值型)");
            } else if (entity.getTime() == null) {
                message.setResult("time值缺失或者格式不对(必须为日期类型，格式为yyyy-MM-dd HH:mm:ss)");
            }
        }
        String account = request.getParameter("account");
        if (account == null) {
            message.setResult("account值缺失");
        }

        if (message.getResult() == null) {
            String sql = "select uid, name, role from " + TableName.getUser() + " where account = ?";
            List values = new ArrayList();
            values.add(account);
            Object userEntity = DBUtil.query(new SjyfiUserDao(), sql, values);
            if (userEntity == null) {
                message.setResult("account用户名不存在");
                message.setStatus(1);
            }
            else {
                SjyfiUserEntity sjyfiUserEntity = (SjyfiUserEntity) userEntity;
                entity.setUid(sjyfiUserEntity.getUid());
                if (DBUtil.insert(DBHelper.getInsertSql(TableName.gettRtGpsPoint(), entity), DBHelper.getSqlValues(entity)) > 0) {
                    List updateGpsList = new ArrayList();
                    entity.setName(sjyfiUserEntity.getName());
                    updateGpsList.add(entity);
                    ShowUserByRealTime.broadcast(Json.writeAsString(updateGpsList));

                    message.setStatus(0);
                    message.setResult("添加成功");
                } else {
                    message.setResult("数据库执行出错");
                    message.setStatus(1);
                }
            }
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
