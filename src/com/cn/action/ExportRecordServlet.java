package com.cn.action;

import com.cn.bean.TTracksEntity;
import com.cn.dao.AEntityDao;
import com.cn.dao.TTracksDao;
import com.cn.test.TestOutput;
import com.cn.util.*;
import com.cn.util.File.FileMerge;
import com.cn.util.File.FileUtil;
import com.cn.util.File.JZipFile;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by acm on 12/8/14.
 */
public class ExportRecordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        List ids = (List) Json.read(request.getParameter("ids"), List.class);

        AEntityDao dao = new TTracksDao();
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select * from ").append(TableName.getTracks()).append(" where trackid in (?");
        for (int i = ids.size() - 1; i > 0; i--) {//生成sql语句
            sbSql.append(", ?");
        }
        sbSql.append(");");

        List entityList = DBUtil.queryMulti(dao, sbSql.toString(), ids);//查询
        String zipFileName = "";
        int isDeleted = 0;
        if (entityList.size() > 0) {
            if (entityList.size() == 1) {
                String tmp = ((TTracksEntity) entityList.get(0)).getPath();
                zipFileName = FileUtil.removeLastSeparator(tmp) + ".kmz";
            } else {
                List<String> paths = new ArrayList<String>();
                for (Object entity : entityList) {
                    String temp = FileUtil.getNestDir(((TTracksEntity) entity).getPath());
                    if (temp != null) {
                        paths.add(temp);//再获取下一层路径，因为轨迹文件在解压是包多含了一层目录
                    }
                }
                String fileName = "routeRecord_" + DateUtil.date2String(new Date(), new SimpleDateFormat("yyyyMMdd_HHmmss"));//生成合并文件所在的文件名
                String mergeFileName =  Config.getUnZipFileDir() + fileName;//完整路径，和解压kmz的文件目录一致
                try {
                    FileMerge merge = new FileMerge();
                    merge.work(paths, mergeFileName);//文件开始合并
                    zipFileName = Config.getZipFileDir() + fileName + ".kmz";//压缩的全路径
                    new JZipFile().work(mergeFileName, zipFileName);//对合并的文件进行压缩
                    isDeleted = 1;//设置删除标志
                } catch (IOException e) {
                    e.printStackTrace();
                    TestOutput.println(e.getMessage());
                } finally {
                    try {
                        FileUtils.deleteDirectory(new File(mergeFileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                        TestOutput.println(e.getMessage());
                    }
                }
            }
        }
        Out out = new Out(response);
        out.printScript("parent.callback(\""+ zipFileName + "\"," + isDeleted + ")");
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
