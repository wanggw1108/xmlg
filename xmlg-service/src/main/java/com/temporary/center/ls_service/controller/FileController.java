package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.SignUtil;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.CurriculumVitaeMapper;
import com.temporary.center.ls_service.dao.UserDao;
import com.temporary.center.ls_service.domain.CurriculumVitae;
import com.temporary.center.ls_service.domain.User;
import org.apache.solr.common.StringUtils;
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

	@Autowired
	CurriculumVitaeMapper curriculumVitaeMapper;
	@Autowired
	UserDao userDao;
	
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
	        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
	        String user_id = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			String path = imageUtil.getFileBasePath(user_id,type);
			MultipartFile multipartFile =  multiRequest.getFile("file");
			File file = new File(path);
			multipartFile.transferTo(file);
			Map<String,String> result = new HashMap<>();
			result.put("imgUrl",imageUtil.getFileUrl(user_id,type));
			String name = null;
			String card = null;
			if(type.equals("4")){
				JSONObject obj = ocrUtil.businessLicense(path);
				card = obj.getJSONObject("words_result").getJSONObject("社会信用代码").getString("words");
				name = obj.getJSONObject("words_result").getJSONObject("单位名称").getString("words");
				obj.put("card",card);
				obj.put("name",name);
				if(StringUtils.isEmpty(card)|| StringUtils.isEmpty(name)){
					json.setSattusCode(StatusCode.AUTH_Company_ERROR);
					return json;
				}

			}
			if(type.equals("2")){
				JSONObject obj = ocrUtil.icardOCR(path);
				card = obj.getJSONObject("words_result").getJSONObject("公民身份号码").getString("words");
				name = obj.getJSONObject("words_result").getJSONObject("姓名").getString("words");
				String age = obj.getJSONObject("words_result").getJSONObject("出生").getString("words");
				String sex = obj.getJSONObject("words_result").getJSONObject("性别").getString("words");
				String minzu = obj.getJSONObject("words_result").getJSONObject("民族").getString("words");
				String huji = obj.getJSONObject("words_result").getJSONObject("住址").getString("words");

				CurriculumVitae vitae = new CurriculumVitae();
				vitae.setCreate_by(Integer.valueOf(user_id));
				vitae = curriculumVitaeMapper.selectOne(vitae);
				if(age.length()==8){
					vitae.setAge(Integer.valueOf(age.substring(0,age.length()-2)));
				}
				vitae.setHousehold_register(huji);
				vitae.setName(name);
				curriculumVitaeMapper.updateByPrimaryKey(vitae);
				obj.put("card",card);
				obj.put("name",name);
				if(StringUtils.isEmpty(card)|| StringUtils.isEmpty(name)){
					json.setSattusCode(StatusCode.AUTH_CHECK_ERROR);
					return json;
				}
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
