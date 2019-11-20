package com.xiaozhuzhijia.webbbs.common.util;

import com.xiaozhuzhijia.webbbs.web.local.LocalUser;
import com.xiaozhuzhijia.webbbs.web.service.impl.CardServiceImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 上传照片
 */
public class UpImgUtil {

    private UpImgUtil(){}

    public static String upImg(MultipartFile file, String path){

        if(StringUtils.isEmpty(path)){
            path = UpImgUtil.class.getClassLoader().getResource("static/images/").getPath();
        }

        String today = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String fileName = LocalUser.get().getUserName() + "_" + file.getOriginalFilename();
        String newDir = path + today + "/" + fileName;
        System.out.println("文件目录：" + newDir);
        String newPath = newDir.substring(newDir.indexOf("/images"));

        File localFile = new File(newDir);
        if(!localFile.getParentFile().exists()){
            localFile.getParentFile().mkdirs();
        }

        //检测文件是否存在
        if(!localFile.exists()){

            try {
                file.transferTo(localFile);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
        return newPath;
    }
}
