package com.cn.util.File;


import com.cn.bean.TTracksEntity;
import com.cn.bean.TTracksPointsEntity;
import com.cn.dao.CheckFileExistDao;
import com.cn.dao.DBHelper;
import com.cn.test.TestOutput;
import com.cn.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by SNNU on 2014/11/5.
 */
/**
 * 对上传文件后文件的解压，读取以及保存。采用线程防止阻塞上传
 */
public class JFile extends Thread {
    static private Queue<String> uploadFilePathQueue = new LinkedList<String>();
    static private Boolean isQueueEmpty = true;

    static public void addUploadFilePath(String filePath) {
        File file = new File(filePath);
        if (false == JFile.checkKMZFileExist(file.getName().substring(0, file.getName().lastIndexOf('.')))) {//添加之前判断数据库中是否已经存在
            uploadFilePathQueue.add(filePath);
            TestOutput.println(filePath + "has added the queue, and queue is empty: " + isQueueEmpty);
            synchronized (isQueueEmpty) {
                if (isQueueEmpty) {
                    isQueueEmpty = false;
                    JFile jFile = new JFile();
                    jFile.start();
                    TestOutput.println(jFile.isAlive());
                }
            }
        }
    }

    static public boolean checkKMZFileExist(String fileName) {
        CheckFileExistDao dao = new CheckFileExistDao();
        List list = new ArrayList();
        list.add( new StringBuilder()
                .append("%")
                .append(fileName)
                .append("%")
                .toString());//去电后缀 .kmz

        String sql = new StringBuilder()
                .append("select trackid from ")
                .append(TableName.getTracks())
                .append(" where path like ?")
                .toString();

        if (null == DBUtil.query(dao, sql, list)) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        TestOutput.println("JFile start");
        while (true) {
            String kmzFileName = uploadFilePathQueue.poll();
            try {
                //解压文件
                String unZipFileName = FileUtil.getDirFromKmzName(kmzFileName);
                new JUnZipFile().work(kmzFileName, unZipFileName);
                try {
                    String nestDir = FileUtil.getNestDir(unZipFileName);
                    TestOutput.println(nestDir);
                    if (nestDir != null) {
                        BaseFileParse fileParse = new TrackDetailFileParse();
                        new JSAXParser().parse(fileParse,nestDir + Config.KMZFileInfo.trackDetailFileName);

                        TTracksEntity tracksEntity = (TTracksEntity) fileParse.getParseObject();
                        tracksEntity.setPath(FileUtil.removeLastSeparator(unZipFileName));
                        tracksEntity.setFilesize((int) new File(kmzFileName).length());
                        tracksEntity.setTrackid(DBUtil.insertAndReturnAutoIncreaseId(
                                DBHelper.getInsertSql(TableName.getTracks(), tracksEntity),
                                DBHelper.getSqlValues(tracksEntity)));

                        String sql = null;
                        List sqlValueList = new ArrayList();
                        fileParse = new PlaceMarkFileParse();
                        new JSAXParser().parse(fileParse, nestDir + Config.KMZFileInfo.routeRecordFileName);
                        List<List<TTracksPointsEntity>> pointList = ((PlaceMarkFileParse) fileParse).getPoints();
                        for (List<TTracksPointsEntity> pointsList : pointList) {
                            for (TTracksPointsEntity point : pointsList) {
                                point.setTrackid(tracksEntity.getTrackid());
                                sqlValueList.add(DBHelper.getSqlValues(point));
                                if (sql == null) {
                                    sql = DBHelper.getInsertSql(TableName.getTrackPoint(), point);
                                }
                            }
                        }
                        if (sql != null) {
                            DBUtil.insertBatch(sql, sqlValueList);
                        }
                    }
                }  catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    TestOutput.println(e.getMessage());
                } catch (SAXException e) {
                    e.printStackTrace();
                    TestOutput.println(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    TestOutput.println(e.getMessage());
                }
            } catch (BuildException e) {
                FileUtils.deleteQuietly(new File(kmzFileName));
                e.printStackTrace();
                TestOutput.println(e.getMessage());
            }

            synchronized (isQueueEmpty) {
                if (uploadFilePathQueue.isEmpty()) {
                    isQueueEmpty = true;
                    break;
                }
            }
        }
        TestOutput.println( "JFile end");
    }
}