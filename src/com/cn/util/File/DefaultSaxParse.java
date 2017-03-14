package com.cn.util.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by SNNU on 2015/3/20.
 */
public class DefaultSaxParse extends DefaultHandler{
    BaseFileParse parse = null;

    public DefaultSaxParse(BaseFileParse fileParse) {
        parse = fileParse;
    }

    public void setFileParse(BaseFileParse fileParse) {
        parse = fileParse;
    }

    @Override
    public void startDocument() throws SAXException {
        parse.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        parse.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        parse.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        parse.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        parse.characters(ch, start, length);
    }
}
