package com.temporary.center.ls_service.controller;

import com.github.pagehelper.PageHelper;
import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.*;
import com.temporary.center.ls_service.dao.FullTimeMapper;
import com.temporary.center.ls_service.domain.FullTime;
import com.temporary.center.ls_service.domain.RecruitmentInfo;
import com.temporary.center.ls_service.service.FullTimeService;
import com.temporary.center.ls_service.service.RecruitmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.Map.Entry;

/**
 * 全职
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/fullTime")
public class FullTimeController {

	private static final Logger logger = LoggerFactory.getLogger(FullTimeController.class);

	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private FullTimeMapper fullTimeService;
	
	@Autowired
	private RecruitmentService recruitmentService;
	
	/**
	 * 全职列表
	 * @return
	 */
	@LogAnno(operateType = "全职列表")
	@RequestMapping(value = "/listTop10.do", method = RequestMethod.GET)
    @ResponseBody
	public Json listTop10() {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="获取招聘列表,"+uuid;
		logger.info(title+",recruitmentList"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			FullTime fullTime=new FullTime();
			fullTime.setCurr(1);
			fullTime.setPageSize(10);
			fullTime.setSort("createTime");
			fullTime.setSortRule(Constant.DESC);
			PageHelper.startPage(1,10);
			Example example = new Example(FullTime.class);
			example.setOrderByClause("createTime desc");
			List<FullTime> list = fullTimeService.selectByExample(example);
			json.setData(list);
			json.setSuc();
			
		}catch(Exception e) {
			e.printStackTrace();
			json.setMsg("操作失败,uuid="+uuid);
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",recruitmentList"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	/**
	 * 全职列表-分页查询 根据创建时间 降序
	 * @return
	 */
	@LogAnno(operateType = "全职列表")
	@RequestMapping(value = "/listPage.do", method = RequestMethod.GET)
    @ResponseBody
	public Json listPage(Integer curr,Integer pageSize,FullTime fullTime) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="获取招聘列表,"+uuid;
		logger.info(title+",recruitmentList"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			//检测方法参数
			if(null==curr) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(curr)");
				return json;
			}
			
			if(null==pageSize) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(pageSize)");
				return json;
			}
			
			//RecruitmentInfo recruitmentInfo=new RecruitmentInfo();
			
			fullTime.setSort("createTime");
			fullTime.setSortRule(Constant.DESC);
			
			int count=fullTimeService.selectCount(fullTime);
			
			if(count==0) {
				json.setSattusCode(StatusCode.NO_DATA);
				return json;
			}
			
			int pageCount=0;
			if(count%pageSize==0) {
				pageCount=count/pageSize;
			}else {
				pageCount=(count/pageSize)+1;
			}
			if(curr>pageCount) {
				curr=pageCount;
			}
			
			fullTime.setPageSize(pageSize);
			fullTime.setCurr(curr);
			
			
			List<FullTime> list = fullTimeService.select(fullTime);
			//设置雇员信誉和雇主信誉
			if(null!=list && list.size()>0) {
				for(FullTime rInfo:list) {
					Long  createby= rInfo.getCreateby();
					if(null==createby || createby.equals("")) {
						logger.warn("createby 为空");
					}else {/*
						String employeeAndBossReputation = redisBean.hget(RedisKey.USER_LIST, createby);
						if(null!=employeeAndBossReputation) {
							String[] employeeAndBossReputationArr = employeeAndBossReputation.split("#");
							if(null!=employeeAndBossReputationArr && employeeAndBossReputationArr.length==2) {
								String employee = employeeAndBossReputationArr[0];
								String boss=employeeAndBossReputationArr[1];
								rInfo.setEmployeeReputation(Integer.parseInt(employee));
								rInfo.setBossReputation(Integer.parseInt(boss));
							}
						}else {
							//设置默认的
							rInfo.setEmployeeReputation(0);
							rInfo.setBossReputation(0);
						}
					*/}
				}
			}
			
			
			PageData pageData=new PageData();
			pageData.setCount(count);
			pageData.setList(list);
			
			json.setData(pageData);
			json.setSuc();
			
		}catch(Exception e) {
			e.printStackTrace();
			json.setMsg("操作失败,uuid="+uuid);
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",recruitmentList"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	
	/**
	 * 发布全职
	 */
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
	public Json fullTime(HttpServletRequest request, HttpServletResponse response,
                         String token, String sign, String timeStamp, String oneType, String twoType,
                         String title, String number, String salaryBegin, String salaryEnd,
                         String salaryUnit, String ageBegin, String ageEnd, String academicRequirements,
                         String experience, String welfare, String detail, String workSpace, String district, String longitude
			, String latitude, String tel, String wx, String email, String contactsName) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String titleString="发布全职,"+uuid;
		logger.info(titleString+",list"+Constant.METHOD_BEGIN);
		
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
			allParamsMap.put("oneType", oneType);
			allParamsMap.put("twoType", twoType);
			allParamsMap.put("title", title);
			allParamsMap.put("number", number);
			allParamsMap.put("salaryBegin", salaryBegin);
			allParamsMap.put("salaryEnd", salaryEnd);
			allParamsMap.put("salaryUnit", salaryUnit);
			allParamsMap.put("ageBegin", ageBegin);
			allParamsMap.put("ageEnd", ageEnd);
			allParamsMap.put("academicRequirements", academicRequirements);
			allParamsMap.put("experience", experience);
			allParamsMap.put("welfare", welfare);
			allParamsMap.put("detail", detail);
			allParamsMap.put("workSpace", workSpace);
			allParamsMap.put("district", district);
			allParamsMap.put("longitude", longitude);
			allParamsMap.put("latitude", latitude);
			allParamsMap.put("tel", tel);
			allParamsMap.put("wx", wx);
			allParamsMap.put("email", email);
			allParamsMap.put("contactsName", contactsName);
			
			Set<String> notValidateArr=new  HashSet<String>();
			notValidateArr.add("salaryUnit");
			notValidateArr.add("ageBegin");
			notValidateArr.add("ageEnd");
			notValidateArr.add("wx");
			
			Json param=validateParam(allParamsMap,notValidateArr);
			if(!StatusCode.SUC.getCode().equals(param.getCode())) {
				json=param;
				return json;
			}
			
			/*MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
 			Iterator<String> iterFile = multiRequest.getFileNames();
 			
 			int i=1;
 			while(iterFile.hasNext()) {
	        	 MultipartFile multipartFile = multiRequest.getFile(iterFile.next());
	        	 String fileName = multipartFile.getOriginalFilename();
	        	 allParamsMap.put("fileName"+i, fileName);
	        	 i++;
	        }*/
 			
 			String property = System.getProperty("tansungWeb.root");
 			
 			String userid = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
 			if(null==userid) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			
			File propertyFile=new File(property+"\\fullTime\\"+userid);
			if(!propertyFile.exists()) {
				propertyFile.mkdirs();
			}
 			
			/*Iterator<String> iter = multiRequest.getFileNames();
			File file = null;
			int relativePathIndex=1;
			Map<String,String> fileMap=new HashMap<String,String>();
			while (iter.hasNext()) {
	            MultipartFile multipartFile = multiRequest.getFile(iter.next());
	            String fileName = multipartFile.getOriginalFilename();
	            logger.info("-----------"+propertyFile+"\\" + fileName+"------------");
	            file = new File(propertyFile+"\\" + fileName);
	            multipartFile.transferTo(file);
	            String relativePath="\\fullTime\\"+userid+"\\file\\"+fileName;
	            fileMap.put("file"+relativePathIndex, relativePath);
	            relativePathIndex++;
	        }*/
			
			RecruitmentInfo fullTime=new RecruitmentInfo(); 
			fullTime.setOneType(oneType);
			fullTime.setTwoType(twoType);
			fullTime.setTitle(title);
			fullTime.setNumber(Integer.parseInt(number));
			fullTime.setSalaryBegin(Float.parseFloat(salaryBegin) );
			fullTime.setSalaryEnd(Float.parseFloat(salaryEnd) );
			fullTime.setSalaryUnit(salaryUnit);
			fullTime.setAgeBegin(Integer.parseInt(ageBegin));
			fullTime.setAgeEnd(Integer.parseInt(ageEnd) );
			fullTime.setAcademicRequirements(academicRequirements);
			fullTime.setExperience(experience);
			fullTime.setWelfare(welfare);
			fullTime.setLongitude(longitude);
			fullTime.setLatitude(latitude);
			fullTime.setTel(tel);
			fullTime.setWx(wx);
			fullTime.setDetail(detail);
			fullTime.setEmail(email);
			fullTime.setContactsName(contactsName);
			fullTime.setDistrict(district);
			/*if(null!=fileMap && fileMap.size()>0) {
				MyCollection<String> values = fileMap.values();
				Iterator<String> iterator = values.iterator();
				String photos="";
				while(iterator.hasNext()) {
					String next = iterator.next();
					photos=photos+next;
				}
				fullTime.setEnvironmentalPhotos(photos);
				json.setData(photos);
			}*/
 			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
 			
 			if(null==userId) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			
 			fullTime.setIsFullTime(Constant.IS_FULL_TIME);
 			fullTime.setCreateby(userId);
 			fullTime.setCreatetime(new Date());
			
 			String createSign = LoginController.createSign(allParamsMap, false);
			
 			if(SignUtil.isOpen()) {
 				if(!createSign.equals(sign)) {
 					json.setData(null);
 					logger.debug("签名="+createSign);
 					json.setSattusCode(StatusCode.SIGN_ERROR);
 					return json;
 				}
 			}
			
			fullTime.setActive(Integer.parseInt(Constant.EFFECTIVE) );
			fullTime.setState(Constant.RECRUITMENTINFO_RUNNING);
			recruitmentService.add(fullTime);
			json.setData("发布成功");
			json.setSuc();
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(titleString+",list"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(titleString+"耗时{0}", endTime-startTime);
		return json;
	}
	
	/**
	 * 校验参数
	 * @param allParamsMap
	 * @param notValidateArr 
	 * @return
	 */
	private Json validateParam(Map<String, String> allParamsMap, Set<String> notValidateArr) {
		Json json=new Json();
		json.setSuc();
		if(null==allParamsMap || allParamsMap.size()==0) {
			json.setSattusCode(StatusCode.PARAMS_NO_NULL);
			return json;
		}
		
		Set<Entry<String, String>> entrySet = allParamsMap.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		
		while(iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			String key = next.getKey();
			//判断是否需要校验
			if(notValidateArr.contains(key)) {
				logger.info("此字段不校验,"+key);
				continue;
			}
			
			String value = next.getValue();
			if(null==value || value.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"("+key+")");
				break;
			}
		}
		return json;
	}
	
	
	/**
	 * 发布全职
	 */
	/*@RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
	public Json add(HttpServletRequest request, HttpServletResponse response,
			String token,String sign,String timeStamp,String oneType,String twoType,
			String title,String number,String salaryBegin,String salaryEnd,
			String salaryUnit,String ageBegin,String ageEnd,String academicRequirements,
			String experience,String welfare,String describe,String workSpace,String longitude
			,String latitude,String tel,String wx,String email) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String titleString="发布全职,"+uuid;
		logger.info(titleString+",list"+Constant.METHOD_BEGIN);
		
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
			allParamsMap.put("oneType", oneType);
			allParamsMap.put("twoType", twoType);
			allParamsMap.put("title", title);
			allParamsMap.put("number", number);
			allParamsMap.put("salaryBegin", salaryBegin);
			allParamsMap.put("salaryEnd", salaryEnd);
			allParamsMap.put("salaryUnit", salaryUnit);
			allParamsMap.put("ageBegin", ageBegin);
			allParamsMap.put("ageEnd", ageEnd);
			allParamsMap.put("academicRequirements", academicRequirements);
			allParamsMap.put("experience", experience);
			allParamsMap.put("welfare", welfare);
			allParamsMap.put("longitude", longitude);
			allParamsMap.put("latitude", latitude);
			allParamsMap.put("tel", tel);
			allParamsMap.put("wx", wx);
			allParamsMap.put("email", email);
			
			Json param=validateParam(allParamsMap);
			if(!StatusCode.SUC.getCode().equals(param.getCode())) {
				return json;
			}
			
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
 			
 			String userid = redisBean.get(RedisKey.USER_TOKEN+token);
 			if(null==userid) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			
			File propertyFile=new File(property+"\\fullTime\\"+userid);
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
	            String relativePath="\\fullTime\\"+userid+"\\file\\"+fileName;
	            fileMap.put("file"+relativePathIndex, relativePath);
	            relativePathIndex++;
	        }
			
			FullTime fullTime=new FullTime(); 
			fullTime.setOneType(oneType);
			fullTime.setTwoType(twoType);
			fullTime.setTitle(title);
 			fullTime.setNumber(Integer.parseInt(number));
			fullTime.setSalaryBegin(Float.parseFloat(salaryBegin) );
			fullTime.setSalaryEnd(Float.parseFloat(salaryEnd) );
			fullTime.setSalaryUnit(salaryUnit);
			fullTime.setAgeBegin(Integer.parseInt(ageBegin));
			fullTime.setAgeEnd(Integer.parseInt(ageEnd) );
			fullTime.setAcademicRequirements(academicRequirements);
			fullTime.setExperience(experience);
			fullTime.setWelfare(welfare);
			fullTime.setLongitude(longitude);
			fullTime.setLatitude(latitude);
			fullTime.setTel(tel);
			fullTime.setWx(wx);
			fullTime.setEmail(email);
			if(null!=fileMap && fileMap.size()>0) {
				MyCollection<String> values = fileMap.values();
				Iterator<String> iterator = values.iterator();
				String photos="";
				while(iterator.hasNext()) {
					String next = iterator.next();
					photos=photos+next;
				}
				fullTime.setEnvironmentalPhotos(photos);
				json.setData(photos);
			}
 			String userId = redisBean.get(RedisKey.USER_TOKEN+token);
 			if(null==userId) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			fullTime.setCreateby(Long.parseLong(userId));
 			fullTime.setCreatetime(new Date());
			
 			String createSign = LoginController.createSign(allParamsMap, false);
			
 			if(SignUtil.isOpen()) {
 				if(!createSign.equals(sign)) {
 					json.setData(null);
 					logger.debug("签名="+createSign);
 					json.setSattusCode(StatusCode.SIGN_ERROR);
 					return json;
 				}
 			}
			
			fullTime.setActive(Integer.parseInt(Constant.EFFECTIVE) );
			fullTimeService.add(fullTime);
			json.setData("全职发布成功");
			json.setSuc();
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(titleString+",list"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(titleString+"耗时{0}", endTime-startTime);
		return json;
	}*/

	/**
	 * 校验参数
	 * @param allParamsMap
	 * @return
	 */
	private Json validateParam(Map<String, String> allParamsMap) {
		Json json=new Json();
		json.setSuc();
		if(null==allParamsMap || allParamsMap.size()==0) {
			json.setSattusCode(StatusCode.PARAMS_NO_NULL);
			return json;
		}
		
		Set<Entry<String, String>> entrySet = allParamsMap.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		
		while(iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			String key = next.getKey();
			String value = next.getValue();
			if(null==value || value.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"("+key+")");
				break;
			}
		}
		return json;
	}
	
	
}
