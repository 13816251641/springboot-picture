package com.lujieni.springbootpicture.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @Auther lujieni
 * @Date 2020-11-29 20:05
 */
@RestController
public class UploadPictureController {

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public boolean uploadPicture(String imgStr) throws Exception {
        String dataPrix = "";
        String data = "";
        if (imgStr == null)
            return false;
        if(imgStr == null || "".equals(imgStr)){
            throw new Exception("上传失败，上传图片数据为空");
        }else{
            String [] d = imgStr.split("base64,");
            if(d != null && d.length == 2){
                dataPrix = d[0];
                data = d[1];
            }else{
                throw new Exception("上传失败，数据不合法");
            }
        }

        String suffix = "";
        if("data:image/jpeg;".equalsIgnoreCase(dataPrix)){//base64编码的jpeg图片数据
            suffix = ".jpg";
        } else if("data:image/x-icon;".equalsIgnoreCase(dataPrix)){//base64编码的icon图片数据
            suffix = ".ico";
        } else if("data:image/gif;".equalsIgnoreCase(dataPrix)){//base64编码的gif图片数据
            suffix = ".gif";
        } else if("data:image/png;".equalsIgnoreCase(dataPrix)){//base64编码的png图片数据
            suffix = ".png";
        }else{
            throw new Exception("上传图片格式不合法");
        }
        String tempFileName = UUID.randomUUID().toString() + suffix;

        String path = "F:/tupian/"+tempFileName;
        File file = new File(path.substring(0,path.lastIndexOf("/")));//这是一个目录的地址
        if(!file.exists())
            file.mkdirs();
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(data);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println(e);
            return false;
        }

    }


}