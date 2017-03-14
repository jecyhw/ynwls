package com.cn.util.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by SNNU on 2015/5/20.
 */
public class DbParser extends BaseFileParse{
    String driver;// = "com.mysql.jdbc.Driver";
    String userName;// = "jecyhw";
    String password;// = "yanghuiwei";
    String url;// = "jdbc:mysql://159.226.15.215:3306/ynwls_db?zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&characterEncoding=utf8&autoReconnect=true";

    static DbParser dbParser;

    static public DbParser getInstance() {
        if (dbParser == null) {
            dbParser = new DbParser();
            try {
                new JSAXParser().parse(dbParser);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dbParser;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    StringBuffer sb = new StringBuffer();

    protected DbParser() {
        try {
            parseFileUri = this.getClass().getResource("/").toURI().getPath() + "configfile/db.xml";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        if ("jdbc".equals(qName)) {
            driver = attrs.getValue("driver");
            userName = attrs.getValue("username");
            password = attrs.getValue("password");
            sb.append(attrs.getValue("protocol"))
                    .append(attrs.getValue("hostname"))
                    .append(":")
                    .append(attrs.getValue("port"))
                    .append("/")
                    .append(attrs.getValue("database"));
        } else if ("param".equals(qName)){
            int attrsLen = attrs.getLength();
            if (attrsLen > 0) {
                sb.append("?")
                        .append(attrs.getQName(0))
                        .append("=")
                        .append(attrs.getValue(0));
                for (int i = 1; i < attrsLen; i++) {
                    sb.append("&")
                            .append(attrs.getQName(i))
                            .append("=")
                            .append(attrs.getValue(i));
                }
                url = sb.toString();
                sb.delete(0, sb.length());
            }
        }
    }


    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        DbParser dbParser = new DbParser();
        new JSAXParser().parse(dbParser);
        System.out.println();
    }
}
