package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.TokenUtil;
import com.temporary.center.ls_service.common.ValidateParam;
import com.temporary.center.ls_service.domain.IdCard;
import com.temporary.center.ls_service.service.IdCardService;
import com.temporary.center.ls_service.service.LogUserService;
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
 * 身份认证
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/idCard")
public class IdCardController {

	private static final LogUtil logger = LogUtil.getLogUtil(IdCardController.class);

	@Autowired
	private IdCardService idCardService;
	
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
			IdCard idCard=new IdCard();
//			dictionaries.setType(Constant.PART_TIME_TYPE);
			List<IdCard> list= idCardService.list(idCard);
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
                    String token, String sign, String timeStamp, String managerName, String managerId) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="提交身份证,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		//token验证
		if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		
		try {
			
			Map<String,String> allParamsMap=new HashMap<String,String>();
			allParamsMap.put("token", token);
			allParamsMap.put("timeStamp", timeStamp);
			allParamsMap.put("managerName", managerName);
			allParamsMap.put("managerId", managerId);
			
			Json validate = ValidateParam.validate(allParamsMap, null, null);
			if(!StatusCode.SUC.getCode().equals(validate.getCode())) {
				return json;
			}
			
			IdCard businessLicense=new IdCard(); 
 			
 			String userId = redisBean.get(token);
 			if(null==userId) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			
 			businessLicense.setCreateby(userId);
 			businessLicense.setCreatetime(new Date());
 			
 			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
 			Iterator<String> iterFile = multiRequest.getFileNames();
 			
 			int i=1;
 			while(iterFile.hasNext()) {
	        	 MultipartFile multipartFile = multiRequest.getFile(iterFile.next());
	        	 String fileName = multipartFile.getOriginalFilename();
	        	 allParamsMap.put("fileName"+i, fileName);
	        	 i++;
	        }
 			
 			String property = System.getProperty("tansungWeb.root");
 			
			File propertyFile=new File(property+"\\businessLicense\\"+userId);
			if(!propertyFile.exists()) {
				propertyFile.mkdirs();
			}
 			
			Iterator<String> iter = multiRequest.getFileNames();
			File file = null;
			int relativePathIndex=1;
			Map<String,String> fileMap=new HashMap<String,String>();
			while (iter.hasNext()) {
	            MultipartFile multipartFile = multiRequest.getFile(iter.next());
	            String fileName = multipartFile.getOriginalFilename();
	            logger.info("-----------"+propertyFile+"\\" + fileName+"------------");
	            file = new File(propertyFile+"\\" + fileName);
	            multipartFile.transferTo(file);
	            String relativePath="\\businessLicense\\"+userId+"\\file\\"+fileName;
	            fileMap.put("file"+relativePathIndex, relativePath);
	            relativePathIndex++;
	        }
			
 			String createSign = LoginController.createSign(allParamsMap, false);
			
			if(!createSign.equals(sign)) {
				logger.debug("签名="+createSign);
				json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
			}
			businessLicense.setAspectUrl(fileMap.get("file1"));
			businessLicense.setPositiveUrl(fileMap.get("file2"));
			businessLicense.setStatus(Constant.IN_AUDIT);
			idCardService.add(businessLicense);
			json.setSuc();
			
			fileMap.put("id", businessLicense.getId()+"");
			json.setData(fileMap);
			
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
