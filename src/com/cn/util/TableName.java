package com.cn.util;

import com.cn.util.File.BaseFileParse;
import com.cn.util.File.JSAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by root on 12/16/14.
 */
public class TableName extends BaseFileParse {

    static String tracks = "t_tracks";
    static String trackPoint = "t_tracks_points";
    static String user = "t_ynwls_user";
    static String tRtGpsPoint = "t_rt_gpspoints";
    static String tRtGpsPointNew = "t_rt_gpspoint_new";

    static {
        try {
            new JSAXParser().parse(new TableName());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String gettRtGpsPointNew() {
        return tRtGpsPointNew;
    }

    public static String gettRtGpsPoint() {
        return tRtGpsPoint;
    }

    public static String getTracks() {
        return tracks;
    }

    public static String getTrackPoint() {
        return trackPoint;
    }

    public static String getUser() {
        return user;
    }

    protected TableName() {
        try {
            parseFileUri = this.getClass().getResource("/").toURI().getPath() + "configfile/table.xml";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        if ("table".equals(qName)) {
            String temp;
            temp = attrs.getValue("tracks");
            if (temp != null) {
                tracks = temp;
            }

            temp = attrs.getValue("user");
            if (temp != null) {
                user = temp;
            }

            temp = attrs.getValue("tracks_points");
            if (temp != null) {
                trackPoint = temp;
            }

            temp = attrs.getValue("rt_gpspoints");
            if (temp != null) {
                tRtGpsPoint = temp;
            }

            temp = attrs.getValue("rt_gpspoint_new");
            if (temp != null) {
                tRtGpsPointNew = temp;
            }
        }
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        TableName dbParser = new TableName();
        new JSAXParser().parse(dbParser);
        System.out.println();
    }
}
