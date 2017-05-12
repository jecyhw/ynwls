package com.cn.action;

import com.cn.bean.TTracksEntity;
import com.cn.dao.AEntityDao;
import com.cn.dao.DBHelper;
import com.cn.dao.TTracksDao;
import com.cn.test.TestOutput;
import com.cn.util.*;
import com.cn.util.File.*;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by acm on 12/8/14.
 */
public class SaveRecordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        List ids = (List)Json.read(request.getParameter("ids"), List.class);

        AEntityDao dao = new TTracksDao();
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select * from ").append(TableName.getTracks()).append(" where trackid in (?");
        for (int i = ids.size() - 1; i > 0; i--) {//生成sql语句
            sbSql.append(", ?");
        }
        sbSql.append(")");

        List entityList = DBUtil.queryMulti(dao, sbSql.toString(), ids);//查询

        List<String> paths = new ArrayList<String>();
        String dirPath = "";
        for (Object entity : entityList) {
            dirPath = FileUtil.removeLastSeparator(((TTracksEntity) entity).getPath());
            String temp = FileUtil.getNestDir(dirPath);
            if (temp != null) {
                paths.add(temp);//再获取下一层路径，因为轨迹文件在解压是包多含了一层目录
            }
        }
        Message message = new Message();
        message.setStatus(1);
        message.setResult("保存失败");

        if (!paths.isEmpty()) {
            String fileName = "routeRecord_" + DateUtil.date2String(new Date(),
                    new SimpleDateFormat("yyyyMMdd_HHmmss"));//生成合并文件所在的文件名
            String mergeFileName = dirPath.substring(0, dirPath.lastIndexOf('/') + 1) + fileName;//合并的文件全路径
            String zipFileName = mergeFileName + ".kmz";//压缩的文件名
            FileMerge merge = new FileMerge();
            try {
                merge.work(paths, mergeFileName);//合并文件
                new JZipFile().work(mergeFileName, zipFileName);//进行压缩
                TrackDetailFileParse fileParse = new TrackDetailFileParse();

                new JSAXParser().parse(fileParse, FileUtil.getNestDir(mergeFileName) + Config.KMZFileInfo.trackDetailFileName);//解析TrackDetail文件
                TTracksEntity entity = (TTracksEntity) fileParse.getParseObject();//获取TrackDetail的相关信息
                entity.setFilesize((int) new File(zipFileName).length());//设置文件大小
                entity.setPath(mergeFileName);//设置文件存放路径
                DBUtil.insert(DBHelper.getInsertSql(TableName.getTracks(), entity), DBHelper.getSqlValues(entity));//保存到数据库

                message.setStatus(0);
                message.setResult("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                FileUtils.deleteQuietly(new File(mergeFileName));//出现异常删除合并文件以及压缩文件
                FileUtils.deleteQuietly(new File(zipFileName));
                TestOutput.println(e.getMessage());
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
