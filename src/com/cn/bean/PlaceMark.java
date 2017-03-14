package com.cn.bean;

import com.cn.util.Config;
import com.cn.util.File.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SNNU on 2015/3/19.
 */
public class PlaceMark {
    static Pattern descPatten = Pattern.compile("(img|video|audio)\\s+src=\"(?:[^/]+/)?(.+?)\"");
    private String name;//两者都有
    private List desc;//关键点的描述
    private TTracksPointsEntity coordinate;//关键点的坐标
    private List<TTracksPointsEntity> route;//轨迹的坐标
    private RouteStyle routeStyle = new RouteStyle();//轨迹的样式

    public String getName() {
        return name;
    }

    public List getDesc() {
        return desc;
    }

    public TTracksPointsEntity getCoordinate() {
        return coordinate;
    }

    public RouteStyle getRouteStyle() {
        return routeStyle;
    }

    public List<TTracksPointsEntity> getRoute() {
        return route;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TTracksPointsEntity setCoordinate(String coordinateText) {
        coordinate = new TTracksPointsEntity();
        StringTokenizer tokenizer = new StringTokenizer(coordinateText, ",", false);
        if (tokenizer.hasMoreElements())
        {
            coordinate.setLongitude(Double.parseDouble(tokenizer.nextToken()));
            coordinate.setLatitude(Double.parseDouble(tokenizer.nextToken()));
            coordinate.setAltitude(Double.parseDouble(tokenizer.nextToken()));

        }
        return coordinate;
    }

    public void setCoordinates(String coordinatesText) {
        route = new ArrayList<TTracksPointsEntity>();
        StringTokenizer tokenizer = new StringTokenizer(coordinatesText, " ", false);
        while (tokenizer.hasMoreElements()) {
            route.add(setCoordinate(tokenizer.nextToken()));
        }
        coordinate = null;//因为在setCoordinate中使用了coordinate，所以最后要设置为null
    }

    /**
     *
     * @param descText 描述文本
     * @param rootPath 父目录完整路径，比如/dir1/dir2/trackRecord/trackRecord.kml 则值为/dir1/dir2/trackRecord或者/dir1/dir2/trackRecord/
     */
    public void setDescription(String descText, String rootPath){
        File parentFile = new File(rootPath);
        String parentName = parentFile.getName();//父目录名称
        StringBuilder sb = new StringBuilder();
        sb.append(parentName);
        File ancestorFile = parentFile.getParentFile();
        while (ancestorFile.getName().equals(parentName)) {
            sb.append(FileUtil.backslash).append(parentName);
            ancestorFile = ancestorFile.getParentFile();
        }
        parentName = FileUtil.addSeparator(sb.toString());

        desc = new ArrayList();
        Matcher matcher = descPatten.matcher(descText);
        while (matcher.find()) {
            String type = matcher.group(1);
            String[] hrefArr = matcher.group(2).split(",");
            String typeDir = "";
            if ("img".equals(type)) {
                typeDir = Config.KMZFileInfo.photoDirectoryName;
            } else if ("video".equals(type)) {
                typeDir = Config.KMZFileInfo.videoDirectoryName;
            } else if ("audio".equals(type)) {
                typeDir = Config.KMZFileInfo.audioDirectoryName;
            }
            for (String href : hrefArr) {
                String result = parentName + typeDir + href;
                if (new File(Config.getUnZipFileDir() + result).exists()) {//判断下该文件是否存在
                    desc.add(Config.getKmzVirtualDir() + result);//虚拟目录+父目录+当前的图片目录
                }
            }
        }
    }

    public void setRouteStyle(RouteStyle routeStyle) {
        this.routeStyle = routeStyle;
    }
}
