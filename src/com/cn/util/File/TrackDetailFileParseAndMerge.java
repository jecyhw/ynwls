package com.cn.util.File;

import com.cn.bean.TTracksEntity;
import com.cn.test.TestOutput;
import com.cn.util.DateUtil;
import org.xml.sax.SAXException;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by SNNU on 2015/3/19.
 */
public class TrackDetailFileParseAndMerge extends BaseFileParse {
    TTracksEntity track = new TTracksEntity();
    StringBuilder builder = new StringBuilder();
    String text;
    String destUri;

    public TrackDetailFileParseAndMerge(String destUri) {
        this.destUri = destUri;
        FileUtil.CreateDirIFNotExist(this.destUri);
        track.setName("");
        track.setAuthor("");
        track.setStarttime(new Timestamp(new java.util.Date().getTime()));
        track.setEndtime(new Timestamp(0));
        track.setLength(0.0);
        track.setMaxaltitude(0.0);
        track.setKeysiteslist("");
        track.setAnnotation("");
    }

    public void startDocument() throws SAXException {

    }

    public void endDocument() throws SAXException {
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        text = builder.toString().trim();
        builder.delete(0, builder.length());
        if (!text.isEmpty()) {
            if ("name".equals(qName)) {
                if (track.getName().indexOf(text) == -1)
                track.setName(track.getName() + ", " + text);
            } else if ("author".equals(qName)) {
                if (track.getAuthor().indexOf(text) == -1) {
                    track.setAuthor(track.getAuthor() + ", " + text);
                }
            } else if ("starttime".equals(qName)) {
                Date date = new Date(DateUtil.string2Date(text).getTime());
                if (track.getStarttime().after(date)) {
                    track.setStarttime(Timestamp.valueOf(text));
                }
            } else if ("endtime".equals(qName)) {
                Date date = new Date(DateUtil.string2Date(text).getTime());
                if (track.getEndtime().before(date)) {
                    track.setEndtime(Timestamp.valueOf(text));
                }
            } else if ("length".equals(qName)) {
                track.setLength(track.getLength() + Double.parseDouble(text));
            } else if ("maxaltitude".equals(qName)) {
                Double maxaltitude = Double.parseDouble(text);
                if (track.getMaxaltitude() < maxaltitude) {
                    track.setMaxaltitude(maxaltitude);
                }
            } else if ("keysiteslist".equals(qName)) {
                if (track.getKeysiteslist().indexOf(text) == -1) {
                    trimComma(text);
                    track.setKeysiteslist(track.getKeysiteslist() + ", " + text);
                }
            } else if ("annotation".equals(qName)) {
                if (track.getAnnotation().indexOf(text) == -1) {
                    track.setAnnotation(track + ", " + text);
                }
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        builder.append(ch, start, length);
    }

    public void createNewTrackDetail(String fileName) throws IOException {
        if (!track.getName().isEmpty()) {
            track.setName(track.getName().substring(2));
        }
        if (!track.getAuthor().isEmpty()) {
            track.setAuthor(track.getAuthor().substring(2));
        }
        if (!track.getKeysiteslist().isEmpty()) {
            track.setKeysiteslist(track.getKeysiteslist().substring(2));
        }
        if (!track.getAnnotation().isEmpty()) {
            track.setAnnotation(track.getAnnotation().substring(2));
        }
        TestOutput.println("TrackDetailFileParseAndMerge: " + track);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destUri + fileName));
        builder.append("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><trackdetail><name>")
                .append(track.getName())
                .append("</name><author>")
                .append(track.getAuthor())
                .append("</author><starttime>")
                .append(DateUtil.date2String(track.getStarttime()))
                .append("</starttime><endtime>")
                .append(DateUtil.date2String(track.getEndtime()))
                .append("</endtime><length>")
                .append(track.getLength())
                .append("</length><maxaltitude>")
                .append(track.getMaxaltitude())
                .append("</maxaltitude><keysiteslist>")
                .append(track.getKeysiteslist())
                .append("</keysiteslist><annotation>")
                .append(track.getAnnotation())
                .append("</annotation></trackdetail>");
        outputStream.write(builder.toString().getBytes("utf-8"));
        outputStream.close();
        builder.delete(0, builder.length());
    }

    String trimComma(String str) {
        if (str.isEmpty())
            return str;
        char[] chs = str.toCharArray();
        int start = 0, end = chs.length;
        for (; start < end; start++) {
            if (chs[start] != ',') {
                break;
            }
        }

        for (; end > start ; end--) {
            if (chs[end - 1] != ',') {
                break;
            }
        }
        return str.substring(start, end);
    }
}
