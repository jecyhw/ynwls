package com.cn.util.File;

import com.cn.test.TestOutput;
import com.cn.util.Config;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acm on 12/8/14.
 */
public class FileMerge {

    /**
     * 合并轨迹文件，kml和xml文件则一定格式进行内容合并
     *
     * @param fileNameList  要合并的文件夹的完整目录
     * @param mergeFileName 合并后的文件夹的完整目录
     */
    public void work(List<String> fileNameList, String mergeFileName) throws IOException {
        try {
            mergeFileName = FileUtil.addSeparator(mergeFileName);
            RouteRecordFileParseAndMerge routeRecordFile = new RouteRecordFileParseAndMerge(mergeFileName);
            TrackDetailFileParseAndMerge trackDetailFile = new TrackDetailFileParseAndMerge(mergeFileName);

            for (String fileName : fileNameList) {
                File[] files = new File(fileName).listFiles();
                for (File file : files) {
                    if (file.getName().contains(".kml")) {
                        new JSAXParser().parse(routeRecordFile, fileName + file.getName());
                    } else if (file.getName().contains(".xml")) {
                        new JSAXParser().parse(trackDetailFile, fileName + file.getName());
                    } else {
                        if (file.isDirectory()) {
                            if (!file.getName().equals(Config.KMZFileInfo.thumbnailDirectoryName)) {//合并文件时过滤掉缩略图文件夹
                                FileUtils.copyDirectory(file, new File(mergeFileName + file.getName()));
                            }
                        } else {
                            FileUtils.copyFile(file, new File(mergeFileName + file.getName()));
                        }
                    }
                }
            }

            routeRecordFile.mergeAndCreateNewTrackRecord(Config.KMZFileInfo.routeRecordFileName);
            trackDetailFile.createNewTrackDetail(Config.KMZFileInfo.trackDetailFileName);
        }  catch (ParserConfigurationException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        }
    }
}
