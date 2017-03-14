package com.cn.util.File;

import com.cn.test.TestOutput;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by SNNU on 2014/11/13.
 */
public class JUnZipFile {
    /**
     *
     * @param inputFileName 要解压的文件名
     * @param unZipFileName 解压的目录
     * @return 返回解压后文件的大小
     */
    public void work(String inputFileName, String unZipFileName) throws BuildException{
        TestOutput.println("JUnZipFile->" + inputFileName + "->" + unZipFileName);
        File srcFile = new File(inputFileName);
        if (srcFile.exists()) {
            Project prj = new Project();
            Expand expand = new Expand();
            expand.setProject(prj);
            expand.setSrc(srcFile);
            expand.setDest(new File(unZipFileName));
            expand.execute();
        }
        TestOutput.println("JUnZipFile end");
    }

    public static void main(String[] args) throws IOException {
        new JUnZipFile().work("C:\\Users/SNNU\\Desktop\\routeRecord_20140526_092338.kmz", "C:\\Users\\SNNU\\Desktop\\routeRecord_20140526_092338");
    }
}
