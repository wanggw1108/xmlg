package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.TokenUtil;
import com.temporary.center.ls_service.common.ValidateParam;
import com.temporary.center.ls_service.dao.IdCardMapper;
import com.temporary.center.ls_service.domain.IdCard;
import com.temporary.center.ls_service.service.IdCardService;
import com.temporary.center.ls_service.service.LogUserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import tk.mybatis.mapper.entity.Example;

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

	private static final Logger logger = LoggerFactory.getLogger(IdCardController.class);

	@Autowired
	private IdCardMapper idCardService;
	
	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private LogUserService logUserService;
	@Autowired
	ImageUtil imageUtil;
	@Autowired
	AipOcrUtil ocrUtil;
	
	
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
			List<IdCard> list= idCardService.selectAll();
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
 			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
 			if(null==userId) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			businessLicense.setCreateby(userId);
 			businessLicense.setCreatetime(new Date());
 			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			MultipartFile card1 = multiRequest.getFile("fileName1");
			File f1 = new File(imageUtil.getFileBasePath(userId+"","2"));
			card1.transferTo(f1);

			JSONObject ocrResult = ocrUtil.icardOCR(imageUtil.getFileBasePath(userId+"","2"));
			JSONObject cardMessage = ocrResult.getJSONObject("words_result");
			String card = cardMessage.getJSONObject("公民身份号码").getString("words");
			String name = cardMessage.getJSONObject("姓名").getString("words");
			if(!managerId.equals(card)||!managerName.equals(name)){
				json.setSattusCode(StatusCode.AUTH_CHECK_ERROR);
				return json;
			}
			MultipartFile card2 = multiRequest.getFile("fileName2");
			File f2 = new File(imageUtil.getFileBasePath(userId+"","3"));
			card2.transferTo(f2);
			businessLicense.setAspectUrl(imageUtil.getFileUrl(userId+"","2"));
			businessLicense.setPositiveUrl(imageUtil.getFileUrl(userId+"","3"));
			businessLicense.setStatus(Constant.IN_AUDIT);
			businessLicense.setManagerId(managerId);
			businessLicense.setManagerName(managerName);
			Example example = new Example(IdCard.class);
			Example.Criteria c = example.createCriteria();
			c.andEqualTo("createby",userId);
			if(idCardService.selectCountByExample(example)>0){
				idCardService.deleteByExample(example);
			}
			idCardService.insert(businessLicense);
			Map<String,Object> fileMap = new HashMap<>();
			fileMap.put("file1",imageUtil.getFileUrl(userId+"","2"));
			fileMap.put("file2",imageUtil.getFileUrl(userId+"","3"));
			fileMap.put("id",businessLicense.getId());
			json.setData(fileMap);
			json.setSuc();
			
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
