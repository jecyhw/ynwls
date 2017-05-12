package com.cn.action;

import com.cn.test.TestOutput;
import com.cn.util.Config;
import com.cn.util.File.FileUtil;
import com.cn.util.File.ResizeImage;
import com.cn.util.Message;
import com.cn.util.Out;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by SNNU on 2015/5/21.
 */
public class ImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        TestOutput.println(path);
        if (path != null) {
            path = FileUtil.replaceSeparator(path);
            int imgTypeIndex = path.indexOf('.');
            if (imgTypeIndex > 0) {
                String imgType = path.substring(imgTypeIndex + 1);
                File imageFile = new File(path);
                if (imageFile.exists()) {
                    String rootPath = imageFile.getAbsolutePath().replaceAll(Config.KMZFileInfo.photoDirectoryName + "[^/]+$", "");//替换掉photo/后的字符
                    String imageName = imageFile.getName();//图片名
                    if (imageFile.exists()) {
                        response.setContentType("image/" + imgType);
                        ServletOutputStream outputStream = response.getOutputStream();
                        String thumbnail = request.getParameter("thumbnail");
                        if (thumbnail == null || thumbnail.equals("false")) {//原图
                            ImageIO.write(ImageIO.read(imageFile), imgType, outputStream);
                        } else {//缩略图
                            File thumbnailFile = new File(new StringBuilder()
                                    .append(rootPath)
                                    .append(Config.KMZFileInfo.thumbnailDirectoryName)
                                    .append(imageName)
                                    .toString());
                            if (!thumbnailFile.exists()) {
                                TestOutput.println(thumbnailFile.getAbsolutePath());
                                File root = thumbnailFile.getParentFile();
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                ResizeImage.work(imageFile, thumbnailFile);
                            }
                            ImageIO.write(ImageIO.read(thumbnailFile), imgType, outputStream);
                        }
                        outputStream.close();
                        return;
                    }
                }
            }
        }
        Message message = new Message();
        message.setStatus(1);
        message.setResult("图片不存在");
        Out out = new Out(response);
        out.printJson(message);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
