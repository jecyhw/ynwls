package com.cn.util.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.util.Date;

/**
 * Created by SNNU on 2015/3/20.
 */
public abstract class BaseFileParse {
    protected String parseFileUri;//需要解析的文件的完整路径

    /**
     *
     * @return 返回解析的数据，默认为null
     */
    public Object getParseObject() {
        return null ;
    }

    public String getParseFileUri() {
        return parseFileUri;
    }

    public void setParseFileUri(String parseFileUri) {
        this.parseFileUri = parseFileUri;
    }

    public void startDocument() throws SAXException {

    }

    public void endDocument() throws SAXException {

    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

    public void characters(char[] ch, int start, int length) throws SAXException {

    }

    static public void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {
        System.out.println(new Date());
    }
}
