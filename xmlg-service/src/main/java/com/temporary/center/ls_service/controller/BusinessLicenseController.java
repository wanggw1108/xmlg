package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.TokenUtil;
import com.temporary.center.ls_service.domain.BusinessLicense;
import com.temporary.center.ls_service.domain.Dictionaries;
import com.temporary.center.ls_service.service.BusinessLicenseService;
import com.temporary.center.ls_service.service.LogUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 营业执照
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/businessLicense")
public class BusinessLicenseController {

	private static final Logger logger = LoggerFactory.getLogger(BusinessLicenseController.class);

	@Autowired
	private BusinessLicenseService businessLicenseService;
	
	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private LogUserService logUserService;
	
	
	@RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
	public Json list() {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="查询营业执照,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			BusinessLicense businessLicense=new BusinessLicense();
//			dictionaries.setType(Constant.PART_TIME_TYPE);
			
			List<Dictionaries> list= businessLicenseService.list(businessLicense);
			json.setData(list);
			
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",list"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
	public Json add(HttpServletRequest request, HttpServletResponse response,
                    String token, String sign, String timeStamp, String name, String code) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="提交营业执照,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		
		//token验证
		boolean validateToken = TokenUtil.validateToken(token, redisBean);
		if(!validateToken) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		
		try {
			
			Map<String,String> allParamsMap=new HashMap<String,String>();
			allParamsMap.put("token", token);
			allParamsMap.put("timeStamp", timeStamp);
			allParamsMap.put("name", name);
			allParamsMap.put("code", code);
			
			
 			BusinessLicense businessLicense=new BusinessLicense(); 
			
 			businessLicense.setName(name);
 			businessLicense.setCode(code);
 			
 			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
 			if(null==userId) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			
 			businessLicense.setCreateby(userId);
 			businessLicense.setCreatetime(new Date());
 			
 			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
 			Iterator<String> iterFile = multiRequest.getFileNames();
 			
 			while(iterFile.hasNext()) {
	        	 MultipartFile multipartFile = multiRequest.getFile(iterFile.next());
	        	 String fileName = multipartFile.getOriginalFilename();
	        	 allParamsMap.put("fileName", fileName);
	        }
 			
 			String property = System.getProperty("tansungWeb.root");
			File propertyFile=new File(property+"\\businessLicense\\"+userId);
			if(!propertyFile.exists()) {
				propertyFile.mkdirs();
			}
 			
			Iterator<String> iter = multiRequest.getFileNames();
			File file = null;
			String relativePath="";
			
			while (iter.hasNext()) {
	            MultipartFile multipartFile = multiRequest.getFile(iter.next());
	            String fileName = multipartFile.getOriginalFilename();
	            logger.info("-----------"+propertyFile+"\\" + fileName+"------------");
	            file = new File(propertyFile+"\\" + fileName);
	            multipartFile.transferTo(file);
	            relativePath="\\businessLicense\\"+userId+"\\file\\"+fileName;
	        }
			
 			String createSign = LoginController.createSign(allParamsMap, false);
			
			if(!createSign.equals(sign)) {
				logger.debug("签名="+createSign);
				json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
			}
			businessLicense.setStatus(Constant.IN_AUDIT);
			businessLicense.setUrl(relativePath);
			businessLicenseService.add(businessLicense);
			json.setSuc();
			Map<String,String> resultMap=new HashMap<String,String>();
			resultMap.put("path", relativePath);
			resultMap.put("id", businessLicense.getId()+"");
			json.setData(resultMap);
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",list"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	
	
}
