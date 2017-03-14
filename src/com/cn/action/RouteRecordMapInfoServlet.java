package com.cn.action;

import com.cn.bean.TTracksEntity;
import com.cn.dao.AEntityDao;
import com.cn.dao.TTracksDao;
import com.cn.test.TestOutput;
import com.cn.util.Config;
import com.cn.util.DBUtil;
import com.cn.util.File.FileUtil;
import com.cn.util.File.JSAXParser;
import com.cn.util.File.PlaceMarkFileParse;
import com.cn.util.Out;
import com.cn.util.TableName;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acm on 11/27/14.
 */
public class RouteRecordMapInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List values = new ArrayList();
        values.add(request.getParameter("id"));

        AEntityDao dao = new TTracksDao();
        String sql ="select * from " + TableName.getTracks() + " where trackid = ?";

        TTracksEntity entity = (TTracksEntity) DBUtil.query(dao, sql, values);

        Out out = new Out(response);
        Object result = new ArrayList();
        if (entity != null)
        {
            String path = FileUtil.getNestDir(entity.getPath());
            if(path != null) {
                String fileName = path + Config.KMZFileInfo.routeRecordFileName;
                try {
                    PlaceMarkFileParse fileParse = new PlaceMarkFileParse();
                    new JSAXParser().parse(fileParse, fileName);
                    result = fileParse.getParseObject();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    TestOutput.println(e.getMessage());
                } catch (SAXException e) {
                    e.printStackTrace();
                    TestOutput.println(e.getMessage());
                }
            }
        }
        out.printJson(result);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
