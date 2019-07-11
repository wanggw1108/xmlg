package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.SignUtil;
import com.temporary.center.ls_service.common.StatusCode;
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

	private static final LogUtil logger = LogUtil.getLogUtil(FileController.class);
	@Value("${imgBasePath}")
	String fileBasePath;

	@Autowired
	private RedisBean redisBean;
	
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST)
    @ResponseBody
	public Json upload(HttpServletRequest request, String  type,String token, String sign, String timeStamp) {
		long startTime = System.currentTimeMillis();
		String title="upload,"+type;
		logger.info(title+",upload"+Constant.METHOD_BEGIN);
		logger.info("token="+token+",sign="+sign+",timeStamp="+timeStamp);
		Json json=new Json ();
		try {
			
			if(!redisBean.exists(token)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			Long time=Long.parseLong(timeStamp);
	        Date currDate=new Date();
	        long countTime=currDate.getTime()-time;
	        if(countTime<0 || countTime>Constant.INTERFACE_TIME_OUT) {
	        	logger.info("时间相差countTime="+countTime);
	    		json.setSattusCode(StatusCode.TIME_OUT_FIVE_MINUTE);
	    		return json;
	        }
	        
	        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
	        
	        Map<String,String> params=new HashMap<String,String>();
	        params.put("time", timeStamp);
	        params.put("token", token);
	        
	        Iterator<String> iterFile = multiRequest.getFileNames();
	        while(iterFile.hasNext()) {
	        	 MultipartFile multipartFile = multiRequest.getFile(iterFile.next());
	        	 String fileName = multipartFile.getOriginalFilename();
	        	 params.put("fileName", fileName);
	        }
	        
	        if(SignUtil.isOpen()) {
	        	String signGenerateByParams=LoginController.createSign(params, false);
		        if(!sign.equals(signGenerateByParams)) {//签名有误
		        	logger.debug("签名不对，需要签名="+signGenerateByParams);
		        	json.setSattusCode(StatusCode.SIG_ERROR);
		        	return json;
		        }
	        }
			
			String property = System.getProperty("tansungWeb.root");
			File propertyFile=new File(property+"\\file");
			if(!propertyFile.exists()) {
				propertyFile.mkdirs();
			}
			
			
	        Iterator<String> iter = multiRequest.getFileNames();
	        File file = null;
	        
	        
	        List<String> fileArray=new ArrayList<String>();
	        while (iter.hasNext()) {
	        	String relativePath="";
	            MultipartFile multipartFile = multiRequest.getFile(iter.next());
	            String fileName = multipartFile.getOriginalFilename();
	            logger.info("-----------"+propertyFile+"\\" + fileName+"------------");
	            file = new File(propertyFile+"\\" + fileName);
	            multipartFile.transferTo(file);
	            relativePath="file\\"+fileName;
	            fileArray.add(relativePath);
	        }
	        json.setData(fileArray);
	        json.setSuc();
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",upload"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	public String getFileBasePath(String user_id,String fileType){
		String path = null;
		switch (fileType){
			case "1": return fileBasePath+"/user/"+user_id+"/header/header_default.png";
			case "2":return fileBasePath+"/user/"+user_id+"/icard/1.png";
			case "3":return fileBasePath+"/user/"+user_id+"/icard/2.png";
			case "4":return fileBasePath+"/user/"+user_id+"/business/header_company.png";
			default:return fileBasePath+"/user/"+user_id+"/header/header_default.png";
		}
	}
	 
}
