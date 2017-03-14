package com.cn.util.File;

import com.cn.bean.*;
import com.cn.test.TestOutput;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SNNU on 2015/3/19.
 */
public class PlaceMarkFileParse extends BaseFileParse {
    List<PlaceMark> placeMarkList = new ArrayList<PlaceMark>();
    PlaceMark placeMark;
    List<RouteStyle> routeStyleList = new ArrayList<RouteStyle>();//用来记录Style
    RouteStyle routeStyle;
    boolean isRoute = false;
    StringBuilder builder = new StringBuilder();
    String text;
    String rootPath;

    public Object getParseObject() {
        Map<String, Object> map = new HashMap<String, Object>();
        List keyPointList = new ArrayList();
        List routeList = new ArrayList();
        for (PlaceMark mark : placeMarkList) {
            if (mark.getRoute() != null)
            {
                RoutePlaceMark routePlaceMark = new RoutePlaceMark();
                routePlaceMark.setName(mark.getName());
                routePlaceMark.setRoute(mark.getRoute());
                routePlaceMark.setRouteStyle(mark.getRouteStyle());
                routeList.add(routePlaceMark);
            } else {
                KeyPointPlaceMark keyPointPlaceMark = new KeyPointPlaceMark();
                keyPointPlaceMark.setName(mark.getName());
                keyPointPlaceMark.setCoordinate(mark.getCoordinate());
                keyPointPlaceMark.setDesc(mark.getDesc());
                int size = keyPointList.size() - 1;
                if (size > -1)
                {
                    KeyPointPlaceMark temp = (KeyPointPlaceMark) keyPointList.get(size);
                    if (temp.getCoordinate().equals(keyPointPlaceMark.getCoordinate())) {
                        temp.merge(keyPointPlaceMark);
                        continue;
                    }
                }
                keyPointList.add(keyPointPlaceMark);
            }
        }
        map.put("keyPointPlaceMarks", keyPointList);
        map.put("routePlaceMarks", routeList);
        TestOutput.println("PlaceMarkFileParse:" + parseFileUri + "-> map");
        return map;
    }

    @Override
    public void setParseFileUri(String parseFileUri) {
        parseFileUri = FileUtil.replaceSeparator(parseFileUri);
        super.setParseFileUri(parseFileUri);
        rootPath = parseFileUri.substring(0, parseFileUri.lastIndexOf(FileUtil.backslash));
    }

    public List<List<TTracksPointsEntity>> getPoints() {
        List<List<TTracksPointsEntity>> lists = new ArrayList<List<TTracksPointsEntity>>();
        for (PlaceMark routePlaceMark : placeMarkList) {
            if (routePlaceMark.getRoute() != null)
                lists.add(routePlaceMark.getRoute());
        }
        return lists;
    }

    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        if (qName.equals("Placemark")) {
            placeMark = new PlaceMark();
        } else if (qName.equals("LineString")) {//  LineString/coordinates
            isRoute = true;
        } else if (qName.equals("Style")) {
            String id = attrs.getValue("id");
            if (id != null) {
                routeStyle = new RouteStyle();
                routeStyle.setId(id);//获取Style的id值
                routeStyleList.add(routeStyle);
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        text = builder.toString().trim();
        builder.delete(0, builder.length());
        if (qName.equals("Placemark")) {//一个Placemark解析完
            placeMarkList.add(placeMark);
            isRoute = false;
            routeStyle = null;
        } else if (qName.equals("coordinates")) {
            if (isRoute)
                placeMark.setCoordinates(text);
            else
                placeMark.setCoordinate(text);
        } else if (qName.equals("name")) {//无子元素，所以可以这样用
            placeMark.setName(text);
        } else if (qName.equals("coordinates")) {
            builder.append(text);
        } else if (qName.equals("description")) {
            placeMark.setDescription(text, rootPath);
        }  else if (qName.equals("color")) {//路线样式颜色
            if (null == routeStyle)//有两种情况，一种路线是引用的样式，另一种是直接设置的样式
                placeMark.getRouteStyle().setColor(text);
            else
                routeStyle.setColor(text);
        } else if (qName.equals("width")) {//路线样式宽度，
            if (null == routeStyle)
                placeMark.getRouteStyle().setWidth(text);
            else
                routeStyle.setWidth(text);
        } else if (qName.equals("styleUrl")) {//说明:引用的样式要在该路线之前设置才能获取到
            text = text.substring(1);//获取该styleUrl的id，去掉开头的#
            for (RouteStyle style : routeStyleList) {
                if (style.getId().equals(text)) {
                    placeMark.setRouteStyle(style);
                    break;
                }
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
         builder.append(ch, start, length);
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        KeyPointPlaceMark mark = new KeyPointPlaceMark();
        mark.setName("111");
        List list = new ArrayList();
        list.add(mark);
        KeyPointPlaceMark mark1 = (KeyPointPlaceMark) list.get(0);
        mark1.setName("2222");
        mark1.setDesc(new ArrayList());
    }
}

