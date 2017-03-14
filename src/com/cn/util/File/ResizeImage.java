package com.cn.util.File;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.omg.CORBA.Environment;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by SNNU on 2015/5/21.
 */
public class ResizeImage {
    static int defaultWidth = 1024;
    static public void work(File src, File dest) {
        try {
            Image srcImg = ImageIO.read(src);
            int height = (int) (srcImg.getHeight(null) * 1.0 * defaultWidth / srcImg.getWidth(null));
            BufferedImage result = new BufferedImage(defaultWidth, height, BufferedImage.TYPE_INT_RGB);
            result.getGraphics().drawImage(srcImg.getScaledInstance(defaultWidth, height, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            FileOutputStream out = new FileOutputStream(dest);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = JPEGCodec.getDefaultJPEGEncodeParam(result);
            param.setQuality(0.75f, true);
            encoder.encode(result, param);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        long s = System.currentTimeMillis();
        //ResizeImage.work("C:\\Users\\SNNU\\Desktop\\20140422_181419.jpg", new FileOutputStream("C:\\Users\\SNNU\\Desktop\\2.jpg"));
        System.out.println(System.currentTimeMillis() - s);
    }
}
