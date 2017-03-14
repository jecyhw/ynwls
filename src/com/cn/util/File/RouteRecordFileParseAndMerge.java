package com.cn.util.File;

import com.cn.test.TestOutput;
import org.apache.commons.io.FileUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Date;

/**
 * Created by SNNU on 2015/3/20.
 */

/**
 * 先将kml文件按plackmark分解，每个PlaceMark将会得到一个对应的文件，分解是默认是关键点的placemark，最后在合并分解后的plackmark文件
 */
public class RouteRecordFileParseAndMerge extends BaseFileParse{
    final String KEY_POINT = "keyPoint";
    final String ROUTE = "route";
    String keyPointPlaceMarkName;
    String routePlaceMarkName;
    int keyPointPlaceMarkSum = -1;
    int routePlaceMarkSum = -1;
    StreamResult xmlStream = null;
    TransformerHandler transformerHandle = null;
    String destUri;
    boolean isRoute = false;
    boolean isTransform = true;

    /**
     *
     * @param destUri 分解得到的文件的目标文件夹
     * @throws TransformerConfigurationException
     */
    public RouteRecordFileParseAndMerge(String destUri) throws TransformerConfigurationException {
        String suffix = new Date().getTime() + "";
        this.destUri = destUri;
        FileUtil.CreateDirIFNotExist(this.destUri);
        keyPointPlaceMarkName = this.destUri + KEY_POINT + suffix;
        routePlaceMarkName = this.destUri + ROUTE + suffix;
        setTransformerHandler();
    }

    void setTransformerHandler() throws TransformerConfigurationException {
        //获取sax生产工厂对象实例
        SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        //获取sax生产处理者对象实例
        transformerHandle = saxTransformerFactory.newTransformerHandler();
        //获取sax生产器
        Transformer transformer = transformerHandle.getTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    }



    /**
     * 默认按照可以pointplacemark来设置文件流
     * @throws FileNotFoundException
     */
    void setTransformerHandleStream()  {
        try {
            xmlStream = new StreamResult(new FileOutputStream(keyPointPlaceMarkName + ++keyPointPlaceMarkSum));
            transformerHandle.setResult(xmlStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        }
    }

    public void startDocument() throws SAXException {
        setTransformerHandleStream();
        transformerHandle.startDocument();
    }

    public void endDocument() throws SAXException {
        transformerHandle.endDocument();
        try {
            xmlStream.getOutputStream().close();//每次结束后都要关闭输出流，否则对后面有影响
            if (isRoute) {
                keyPoint2RoutePlaceMark();
            }
        } catch (IOException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("Document".equals(qName) || "kml".equals(qName) || "Folder".equals(qName)) {
            isTransform = false;
        } else {
            if ("Placemark".equals(qName)) {//是Placemark重新输出个文件
                endDocument();//必须有这句，和startDocument中一一对应
                startDocument();
            } else if ("LineString".equals(qName)) {
                isRoute = true;
            }
            transformerHandle.startElement(uri, localName, qName, attributes);
            isTransform = true;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("Document".equals(qName) || "kml".equals(qName) || "Folder".equals(qName)) {
            isTransform = false;
        } else {
            transformerHandle.endElement(uri, localName, qName);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTransform) {
            transformerHandle.characters(ch, start, length);
        }
    }

    /**
     * 将某个关键点的plackmark文件名更改为路线的placemark
     */
    public void keyPoint2RoutePlaceMark() {
        File keyPointFile = new File(keyPointPlaceMarkName + keyPointPlaceMarkSum--);//修改后，keyPointPlaceMarkSum需要减1
        keyPointFile.renameTo(new File(routePlaceMarkName + ++routePlaceMarkSum));//对应routePlaceMarkSum数加1
        isRoute = false;
    }

    /**
     * 合并解析的kml文件
     * @param fileName 合并的文件名，目录默认为分解的plackmark的存放目录
     */
    public void mergeAndCreateNewTrackRecord(String fileName) {
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destUri + fileName));
            outputStream.write("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><Document>".getBytes("utf-8"));
            outputStream.flush();
            mergeKeyPointPlaceMark(outputStream);
            outputStream.write("<Folder>".getBytes("utf-8"));
            mergeRoutePlaceMark(outputStream);
            outputStream.write("</Folder></Document>".getBytes("utf-8"));
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        }
    }

    void mergeKeyPointPlaceMark(BufferedOutputStream outputStream) throws IOException {
        for (int i = 0; i <= keyPointPlaceMarkSum; i++) {
            File file = new File(keyPointPlaceMarkName + i);
            outputStream.write(FileUtils.readFileToByteArray(file));
            outputStream.flush();
            file.delete();
        }
    }

    void mergeRoutePlaceMark(BufferedOutputStream outputStream) throws IOException {
        for (int i = 0; i <= routePlaceMarkSum; i++) {
            File file = new File(routePlaceMarkName + i);
            outputStream.write(FileUtils.readFileToByteArray(file));
            outputStream.flush();
            file.delete();
        }
    }
}
