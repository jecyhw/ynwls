package com.cn.util.File;

import com.cn.test.TestOutput;

import java.io.File;
import java.io.IOException;

/**
 * Created by SNNU on 2014/11/13.
 */
public final class FileUtil {
    static public String backslash = "/";//在所有使用backslash的地方，都需要先将\替换成/,因为在window下路径分隔符是\
    public static File CreateDirIFNotExist(String dir) {
        File file = new File(dir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        return  file;
    }

    public static String addSeparator(String dir) {
        dir = replaceSeparator(dir);
        if (!dir.endsWith(backslash))//由于windows下文件分隔符是\
            dir += backslash;
        return dir;
    }

    public static String removeLastSeparator(String dir) {
        dir = replaceSeparator(dir);
        if (dir.endsWith(backslash)) {
            return dir.substring(0, dir.length() - 1);
        }
        return dir;
    }
    /**
     * 如果解压后的文件夹嵌套了文件夹，则返回嵌套的文件目录,文件路径最后带/
     * @param dir
     * @return
     */
    public static String getNestDir(String dir) {//获取kml.xml的上一级目录
        dir = replaceSeparator(dir);
        File dirFile = new File(dir);
        dir = dirFile.getAbsolutePath();
        if (dirFile.exists()) {
            String dirName = dir + backslash;
            String fileName = dirFile.getName();
            while (true) {
                File nestDir = new File(dirName + fileName);
                if (nestDir.exists() && nestDir.isDirectory()) {
                      dirName += fileName + backslash;
                } else {
                    return dirName;
                }
            }
        }
        //new JUnZipFile().work(dir + ".kmz", dir);//当文件不存在时，看是否存在该名字的压缩文件，是进行解压，在获取目录
        return null;
    }

    /**
     * 去掉.kmz文件名的后缀名,并且以/结尾的路径名
     * @param kmzFileName
     * @return
     */
    public static String getDirFromKmzName(String kmzFileName) {
        kmzFileName = replaceSeparator(kmzFileName);
        return kmzFileName.substring(0, kmzFileName.lastIndexOf('.')) + backslash;
    }

    public static String replaceSeparator(String path) {
        return path.replace('\\', '/');
    }

    public static void main(String[] args) {
        //copyFile(new File("/home/acm/markers.png"), new File("/home/acm/1.png"));
    }
}
