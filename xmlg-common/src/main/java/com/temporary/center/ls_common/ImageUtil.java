package com.temporary.center.ls_common;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-17-15:26
 */
@Component
public class ImageUtil {

    @Value("${fileBasePath}")
    String fileBasePath;
    @Value("${staticUrlPath}")
    String staticUrlPath;
    public  String getFileBasePath(String user_id,String fileType){
        String path;
        String abspath;
        switch (fileType){
            case "1": {
                path = StringUtils.isEmpty(user_id)?fileBasePath+"/user/header":fileBasePath+"/user/"+user_id+"/header";
                abspath = path+"/header_default.png";
                break;
            }
            case "2":{
                path = StringUtils.isEmpty(user_id)?fileBasePath+"/user/icard":fileBasePath+"/user/"+user_id+"/icard";
                abspath = path+"/front.png";
                break;
            }
            case "3":{
                path =  StringUtils.isEmpty(user_id)?fileBasePath+"/user/icard":fileBasePath+"/user/"+user_id+"/icard";
                abspath = path+"/back.png";
                break;
            }
            case "4":{
                path =  StringUtils.isEmpty(user_id)?fileBasePath+"/user/business":fileBasePath+"/user/"+user_id+"/business";
                abspath = path+"/header_company.png";
                break;
            }
            case "5":{
                path =  StringUtils.isEmpty(user_id)?fileBasePath+"/user/business":fileBasePath+"/user/"+user_id+"/business";
                abspath = path+"/qualification1.png";
                break;
            }
            case "6":{
                path =  StringUtils.isEmpty(user_id)?fileBasePath+"/user/business":fileBasePath+"/user/"+user_id+"/business";
                abspath = path+"/qualification2.png";
                break;
            }
            case "7":{
                path =  StringUtils.isEmpty(user_id)?fileBasePath+"/user/business":fileBasePath+"/user/"+user_id+"/business";
                abspath = path+"/qualification3.png";
                break;
            }
            case "8":{
                path =  StringUtils.isEmpty(user_id)?fileBasePath+"/user/business":fileBasePath+"/user/"+user_id+"/business";
                abspath = path+"/auth.png";
                break;
            }
            default:{
                path =  StringUtils.isEmpty(user_id)?fileBasePath+"/user/header":fileBasePath+"/user/"+user_id+"/header";
                abspath = path+"/header_default.png";
                break;
            }
        }
        File f = new File(path);
        if(!f.exists()){
            f.mkdirs();
        }
        return abspath;
    }
    public  String getFileUrl(String user_id,String fileType){
        String filePath = getFileBasePath(user_id,fileType);
        String fileUrl = filePath.replace(fileBasePath,staticUrlPath);
        return fileUrl;
    }

}
