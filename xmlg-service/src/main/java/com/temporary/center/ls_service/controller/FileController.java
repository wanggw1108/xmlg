package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.SignUtil;
import com.temporary.center.ls_service.common.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * 文件上传处理
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/file")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	@Value("${fileBasePath}")
	String fileBasePath;
	@Value("${staticUrlPath}")
	String staticUrlPath;

	@Autowired
	private RedisBean redisBean;
	
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST)
    @ResponseBody
	public Json upload(HttpServletRequest request, String  type,String token, String sign, String timeStamp) {
		String title="upload,"+type;
		logger.info(title+",upload"+Constant.METHOD_BEGIN);
		logger.info("token="+token+",sign="+sign+",timeStamp="+timeStamp);
		Json json=new Json ();
		try {
			
			if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			long startTime = Long.valueOf(timeStamp);
	        if((System.currentTimeMillis() - startTime)>Constant.INTERFACE_TIME_OUT) {
	    		json.setSattusCode(StatusCode.TIME_OUT_FIVE_MINUTE);
	    		return json;
	        }
	        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
	        String user_id = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			String path = getFileBasePath(user_id,type);
	        Iterator<String> iter = multiRequest.getFileNames();
	        if(iter.hasNext()){
				MultipartFile multipartFile = multiRequest.getFile(iter.next());
				logger.info("-------create----"+path+"------------");
				File file = new File(path);
				multipartFile.transferTo(file);
			}
	        json.setData(path.replace(fileBasePath,staticUrlPath));
	        json.setSuc("上传成功");
		}catch(Exception e) {
			logger.error("upload error",e);
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",upload"+Constant.METHOD_END);
		return json;
	}
	public  String getFileBasePath(String user_id,String fileType){
		String path;
		String abspath;
		switch (fileType){
			case "1": {
				path =  fileBasePath+"/user/"+user_id+"/header";
				abspath = path+"/header_default.png";
			}
			case "2":{
				path = fileBasePath+"/user/"+user_id+"/icard";
				abspath = path+"/1.png";
			}
			case "3":{
				path = fileBasePath+"/user/"+user_id+"/icard";
				abspath = path+"/2.png";
			}
			case "4":{
				path = fileBasePath+"/user/"+user_id+"/business";
				abspath = path+"/header_company.png";
			}
			default:{
				path =  fileBasePath+"/user/"+user_id+"/header";
				abspath = path+"/header_default.png";
			}
		}
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		return abspath;
	}
	 
}
