package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.SignUtil;
import com.temporary.center.ls_service.common.StatusCode;
import org.json.JSONObject;
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
	@Autowired
	ImageUtil imageUtil;
	@Autowired
	AipOcrUtil ocrUtil;

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
			String path = imageUtil.getFileBasePath(user_id,type);
			MultipartFile multipartFile =  multiRequest.getFile("file");
			File file = new File(path);
			multipartFile.transferTo(file);
			JSONObject result = new JSONObject();
			result.put("imgUrl",imageUtil.getFileUrl(user_id,type));
			String name = null;
			String card = null;
			if(type.equals("4")){
				JSONObject obj = ocrUtil.businessLicense(path);
				card = obj.getJSONObject("words_result").getJSONObject("社会信用代码").getString("words");
				name = obj.getJSONObject("words_result").getJSONObject("单位名称").getString("words");
				obj.put("card",card);
				obj.put("name",name);

			}
			if(type.equals("2")){
				JSONObject obj = ocrUtil.icardOCR(path);
				card = obj.getJSONObject("words_result").getJSONObject("公民身份号码").getString("words");
				name = obj.getJSONObject("words_result").getJSONObject("姓名").getString("words");
				obj.put("card",card);
				obj.put("name",name);

			}
			result.put("card",card);
			result.put("name",name);
			json.setData(result);
	        json.setSuc("上传成功");
		}catch(Exception e) {
			logger.error("upload error",e);
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",upload"+Constant.METHOD_END);
		return json;
	}
	 
}
