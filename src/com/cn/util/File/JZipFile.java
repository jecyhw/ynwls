package com.cn.util.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;

/**
 * Created by SNNU on 2014/11/13.
 */
public class JZipFile {
    /**
     *
     * @param srcFileName 要压缩的文件名
     * @param zipFileName  压缩后的文件名
     */
    public void work(String srcFileName, String zipFileName) {
        File srcFile = new File(srcFileName);
        if (srcFile.exists()) {
            Project prj = new Project();

            Zip zip = new Zip();
            zip.setProject(prj);
            zip.setDestFile(new File(zipFileName));

            FileSet fileSet = new FileSet();
            fileSet.setProject(prj);
            fileSet.setDir(srcFile);
            zip.addFileset(fileSet);
            zip.execute();
        }
    }
}
