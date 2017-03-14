package com.cn.util;

import com.cn.util.File.BaseFileParse;
import com.cn.util.File.FileUtil;
import com.cn.util.File.JSAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by SNNU on 2014/11/5.
 */
public final class Config extends BaseFileParse{
    static String uploadDir = "/var/gpstracks/";
    static String uploadTempDir = uploadDir;
    static String unZipFileDir = uploadDir;
    static String zipFileDir = uploadDir;
    static String kmzVirtualDir = "/";

    static {
        try {
            new JSAXParser().parse(new Config());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUploadDir() {
        return uploadDir;
    }

    public static String getZipFileDir() {
        return zipFileDir;
    }

    public static String getUnZipFileDir() {
        return unZipFileDir;
    }

    public static String getUploadTempDir() {
        return uploadTempDir;
    }

    public static String getKmzVirtualDir() {
        return kmzVirtualDir;
    }

    protected Config() {
        try {
            parseFileUri = this.getClass().getResource("/").toURI().getPath() + "configfile/upload.xml";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        if ("config".equals(qName)) {
            String temp = attrs.getValue("uploadDir");
            if (temp != null) {
                uploadDir = FileUtil.addSeparator(temp);

                temp = attrs.getValue("uploadTempDir");
                uploadTempDir = temp == null ? uploadDir : FileUtil.addSeparator(temp);

                temp = attrs.getValue("zipFileDir");
                zipFileDir = temp == null ? uploadDir : FileUtil.addSeparator(temp);

                temp = attrs.getValue("unZipFileDir");
                unZipFileDir = temp == null ? uploadDir : FileUtil.addSeparator(temp);
            }

            temp = attrs.getValue("kmzVirtualPath");
            if (temp != null) {
                kmzVirtualDir = temp;
            }
        }
    }

    public class KMZFileInfo{
        static final public String routeRecordFileName = "RouteRecord.kml";
        static final public String trackDetailFileName = "TrackDetail.xml";
        static final public String videoDirectoryName = "video/";
        static final public String photoDirectoryName = "photo/";
        static final public String audioDirectoryName = "audio/";
        static final public String thumbnailDirectoryName = "thumbnail/";
    }
}
