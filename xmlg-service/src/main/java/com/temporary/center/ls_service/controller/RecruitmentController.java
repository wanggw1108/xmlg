package com.temporary.center.ls_service.controller;

import com.alibaba.fastjson.JSONObject;
import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.*;
import com.temporary.center.ls_service.common.Dictionary;
import com.temporary.center.ls_service.dao.JoinMapper;
import com.temporary.center.ls_service.dao.WorkTimeIntervalMapper;
import com.temporary.center.ls_service.dao.WorkTimeMapper;
import com.temporary.center.ls_service.domain.*;
import com.temporary.center.ls_service.result.MyRecruitResult;
import com.temporary.center.ls_service.service.*;
import org.apache.solr.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 招聘信息
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/recruitment")
public class RecruitmentController {

	private static final LogUtil logger = LogUtil.getLogUtil(RecruitmentController.class);

	@Value("${staticUrlPath}")
	String staticUrlPath;
	static Map<Integer,String> stateMap=new HashMap<>();
	static {
		stateMap.put(1, "进行中");
		stateMap.put(2, "下架");
		stateMap.put(3, "完结");
	}
	
	
	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private RecruitmentService recruitmentService;
	
	@Autowired
	private LogUserService logUserService;
	
	@Autowired
	private WorkTimeMapper workTimeService;
	
	@Autowired
	private WorkTimeIntervalMapper workTimeIntervalService;
	
	@Autowired
	private JoinMapper joinService;
	
	
	
	
	
	/**
	 * 我的招聘
	 * @return
	 */
	@RequestMapping(value = "/myRecruit.do", method = RequestMethod.GET)
    @ResponseBody
	public Json myRecruit(String token,String state,Integer curr ,Integer pageSize) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="我的招聘,"+uuid;
		logger.info(title+",recruitmentList"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			//检测token 
			if(null==token || token.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
				return json;
			}
			if(null==curr) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(curr)");
				return json;
			}
			if(null==pageSize) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(pageSize)");
				return json;
			}
			if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
				logger.info("token过期");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			
			
			List<MyRecruitResult> myRecruitResultList=new ArrayList<>();
			
			RecruitmentInfo recruitmentInfo=new RecruitmentInfo();
			recruitmentInfo.setCreateby(redisBean.hget(RedisKey.USER_TOKEN+token,"user_id"));
			List<RecruitmentInfo> list = recruitmentService.list(recruitmentInfo);
			
			Long countByParams = recruitmentService.countByParams(recruitmentInfo);
			
			for (RecruitmentInfo recruitmentInfo2:list) {
				MyRecruitResult myRecruitResult=new MyRecruitResult();
				myRecruitResult.setBasePay(recruitmentInfo2.getBasePay().toString());//
				myRecruitResult.setBasePayUnit(recruitmentInfo2.getBasePayUnit());//工资单位
				String key =RedisKey.RECRUITMENTINFO_COUNT+ recruitmentInfo2.getId();
				Long browseTime=0L;
				if(redisBean.exists(key)) {
					browseTime=Long.parseLong(redisBean.get(key));
				}
				myRecruitResult.setBrowse(browseTime.toString());//
				myRecruitResult.setNeedRecruitNumber(recruitmentInfo2.getNumber().toString());//需要招聘人数
				Join join1=new Join();
				join1.setResumeId(recruitmentInfo2.getId());
				join1.setState(Constant.INTERVIEW_SUC);
				Example joinExample = new Example(Join.class);
				Example.Criteria c = joinExample.createCriteria();
				c.andEqualTo("resume_id",recruitmentInfo2.getId());
				c.andEqualTo("status",Constant.INTERVIEW_SUC);
				int countByParam = joinService.selectCountByExample(joinExample);
				myRecruitResult.setRecruitedNumber(countByParam+"");//已经招聘的人数
				myRecruitResult.setRecruitId(recruitmentInfo2.getId().toString());//招聘信息的ID
				myRecruitResult.setReleaseTime(DateUtil.Date2DTstring(recruitmentInfo2.getCreatetime()));//发布时间
				Join join2=new Join();
				join2.setResumeId(recruitmentInfo2.getId());
				Example joinExample2 = new Example(Join.class);
				Example.Criteria c2 = joinExample.createCriteria();
				c2.andEqualTo("resume_id",recruitmentInfo2.getId());
				int countByParam2 = joinService.selectCountByExample(joinExample2);
				myRecruitResult.setSignUpNumber(countByParam2+"");//报名人数
				myRecruitResult.setState(stateMap.get(recruitmentInfo2.getState()));//招聘信息的状态 0：全部；1：进行中；2：下架；3：完结
				myRecruitResult.setTitle(recruitmentInfo2.getTitle());//招聘信息的标题
				myRecruitResultList.add(myRecruitResult);
			}
			
			
			Map<String, Object> result=new HashMap<>();
			result.put("list", myRecruitResultList);
			result.put("count", countByParams);
			json.setData(result);
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
	 * 招聘列表
	 * @return
	 */
	@LogAnno(operateType = "招聘列表")
	@RequestMapping(value = "/listTop10.do", method = RequestMethod.GET)
    @ResponseBody
	public Json listTop10() {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="获取招聘列表,"+uuid;
		logger.info(title+",recruitmentList"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			
			
			RecruitmentInfo recruitmentInfo=new RecruitmentInfo();
			recruitmentInfo.setCurr(1);
			recruitmentInfo.setPageSize(10);
			recruitmentInfo.setSort("createTime");
			recruitmentInfo.setSortRule(Constant.DESC);
			recruitmentInfo.setActive(0);
			List<RecruitmentInfo> list = recruitmentService.list(recruitmentInfo);
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
	 * 职位列表
	 * @param recruitmentInfo
	 * @param bindingResult
	 * @param recruitmentInfo
	 * @return
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
    @ResponseBody
	public Json list(RecruitmentInfo recruitmentInfo,BindingResult bindingResult) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		Json json=new Json ();
		String title="";
		try {
			title="获取招聘列表,"+uuid;
			logger.info(title+",recruitmentList"+Constant.METHOD_BEGIN);
			logger.info(title+",入参："+ClassUtil.printlnFieldValue(recruitmentInfo));
			
			if(bindingResult.hasErrors()){
	            for (FieldError fieldError : bindingResult.getFieldErrors()) {
	                logger.info(fieldError.getDefaultMessage());
	            }
	            return json;
	        }
			
			//检测方法参数
			Integer curr = recruitmentInfo.getCurr();
			
			if(null==curr) {
				curr=1;
			}
			
			Integer pageSize = recruitmentInfo.getPageSize();
			if(null==pageSize) {
				pageSize=10;
			}
			
			//纬度不能为空
			String latitude = recruitmentInfo.getLatitude();
			if(null==latitude || latitude.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				return json;
			}
			
			//经度不能为空
			String longitude = recruitmentInfo.getLongitude();
			if(null==longitude || longitude.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				return json;
			}
			 
			String sort = recruitmentInfo.getSort();
			if(null==sort || sort.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(sort)");
				return json;
			}
			
			
			
			recruitmentInfo.setLatitude(null);
			recruitmentInfo.setLongitude(null);
			Long count=recruitmentService.countByParams(recruitmentInfo);
			
			if(null==count || count.equals(0L)) {
				json.setSattusCode(StatusCode.NO_DATA);
				return json;
			}
			
			Long pageCount=0L;
			if(count%pageSize==0) {
				pageCount=count/pageSize;
			}else {
				pageCount=(count/pageSize)+1;
			}
			if(curr>pageCount) {
				curr=pageCount.intValue();
			}
			
			recruitmentInfo.setPageSize(pageSize);
			recruitmentInfo.setCurr(curr);
			
			recruitmentInfo.setLatitude(latitude);
			recruitmentInfo.setLongitude(longitude);
			
			
			if(null!=recruitmentInfo.getSort() && !"".equals(recruitmentInfo.getSort())) {
				//按时间，距离、工资
				if("1".equals(recruitmentInfo.getSort())) {
					recruitmentInfo.setSort("createTime");
				}else if("2".equals(recruitmentInfo.getSort())) {
					recruitmentInfo.setSort("distance");
				}else if("3".equals(recruitmentInfo.getSort())) {
					recruitmentInfo.setSort("hourly_wage");
				}
			}else {//默认按时间排序
				recruitmentInfo.setSort("createTime");
			}
			if(StringUtils.isEmpty(recruitmentInfo.getSortRule())){
				recruitmentInfo.setSortRule("desc");
			}
			recruitmentInfo.setActive(0);//
			
			List<RecruitmentInfo> list =recruitmentService.listIndex(recruitmentInfo);
			
			//设置雇员信誉和雇主信誉
			if(null!=list && list.size()>0) {
				for(RecruitmentInfo rInfo:list) {
					logger.info(uuid+"返回的记录:"+ClassUtil.printlnFieldValue(rInfo));


					String welfare = rInfo.getWelfare();
					StringBuffer welfareSb=new StringBuffer();

					if(null!=welfare && !"".equals(welfare)) {
						//福利翻译
						String[] welfareArr = welfare.split(",");
						for(String wel:welfareArr) {
							welfareSb.append(Dictionary.welfare.get(Integer.parseInt(wel))+",");
						}
					}

					if(welfareSb.length()>0) {
						rInfo.setWelfare(welfareSb.substring(0, welfareSb.length()-1));
					}
					rInfo.getWelfare();
					
					//获取浏览次数
					String key =RedisKey.RECRUITMENTINFO_COUNT+ rInfo.getId();
					Long browseTime=0L;
					if(redisBean.exists(key)) {
						browseTime=Long.parseLong(redisBean.get(key));
					}
					rInfo.setBrowseTime(browseTime);
					String createby = rInfo.getCreateby();
					User user = new User();
					user.setId(Integer.valueOf(createby));
					user = logUserService.selectOne(user);
					if(null==createby || createby.equals("")) {
						logger.warn("createby 为空");
					}else {
						rInfo.setCreateSex(user.getSex()+"");//招聘人的性别 1是男；2是女
						rInfo.setUserImageUrl(staticUrlPath+user.getUserImageUrl());//用户头像
						rInfo.setEmployeeReputation(user.getEmployeeReputation());
						rInfo.setBossReputation(user.getBossReputation());
					}
				}
			}
			
			
			PageData pageData=new PageData(list,count,curr,pageSize,Integer.valueOf(pageCount+""));
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
	 * 职位 详情
	 * @return
	 */
	@RequestMapping(value = "/listPage.do", method = RequestMethod.GET)
    @ResponseBody
	public Json listPage(Integer id,String token,String timestamp) {
		Json json = new Json();
		if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;

		}
		String key =RedisKey.RECRUITMENTINFO_COUNT+ id;
		boolean exists = redisBean.exists(key);
		if(!exists) {
			redisBean.incr(key);
		}else {
			redisBean.incrBy(key, 1);
		}
		RecruitmentInfo info = recruitmentService.findById(Long.valueOf(id));
		int userid = Integer.valueOf(info.getCreateby());
		User user = new User();
		user.setId(userid);
		user = logUserService.selectOne(user);
		info.setUserImageUrl(staticUrlPath+user.getUserImageUrl());
		json.setData(info);
		json.setSuc();
		return json;
	}
	
	/**
	 * 发布兼职
	 */
	@Transactional
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
    public Json add(@RequestBody String params) {

		JSONObject obj = JSONObject.parseObject(params);
		String token = obj.getString("token");
		String timeStamp = obj.getString("timeStamp");
		RecruitmentInfo recruitmentInfo = JSONObject.toJavaObject(obj,RecruitmentInfo.class);
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="发布兼职,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		
		//token验证
		boolean validateToken = TokenUtil.validateToken(token, redisBean);
		if(!validateToken) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		
		try {
			
 			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
 			if(null==userId) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			
 			recruitmentInfo.setCreateby(userId);
 			recruitmentInfo.setCreatetime(new Date());
 			
 			//验证不为空的参数
			json=ValidateParam.validateParamNotNull(recruitmentInfo);
 			if(!json.getCode().equals(StatusCode.SUC.getCode())) {
 				return json;
 			}
 			//计算时薪
 			Float hourlyWage = Dictionary.switchUnit(Dictionary.wagesUnit.get(Integer.valueOf(recruitmentInfo.getBasePayUnit())), recruitmentInfo.getBasePay());
 			recruitmentInfo.setHourlyWage(hourlyWage);
			recruitmentInfo.setActive(Integer.parseInt(Constant.EFFECTIVE) );
			recruitmentInfo.setRecruitment(0);//已经招聘人数 默认为0
			
			//拆分工作时间 工作开始时间 如果有多条以英文逗号隔开 如：2019-06-13,2019-06-17
			String[] workingStartTimeArr = recruitmentInfo.getWorkingStartTime().split(",");
			String[] workingEndTimeArr = recruitmentInfo.getWorkingEndTime().split(",");
			
			recruitmentInfo.setState(Constant.RECRUITMENTINFO_RUNNING);
			recruitmentInfo.setActive(0);
			recruitmentService.add(recruitmentInfo);
			
			Integer id = recruitmentInfo.getId();
			
			for(int i=0;i<workingStartTimeArr.length;i++) {
				//工作时间
				WorkTime workTime=new  WorkTime();
				workTime.setRecruitmentInfoId(id);
				workTime.setStartTime(DateUtil.parase(workingStartTimeArr[i], "yyyy-MM-dd"));
				workTime.setEndTime(DateUtil.parase(workingEndTimeArr[i], "yyyy-MM-dd"));
				workTime.setSort(i);
				workTimeService.insert(workTime);
			}
			
			if(null!= recruitmentInfo.getWorkingTime() && !"".equals(recruitmentInfo.getWorkingTime())) {
				//上班时段 如果有多条以英文逗号隔开 如：9:00-12:00,2:00-6:00
				String[] workingTimeArr = recruitmentInfo.getWorkingTime().split(",");
				//工作时段 如果有多条以英文逗号隔开 如：9:00-12:00,2:00-6:00
				for(int i=0;i<workingTimeArr.length;i++) {
					
					String wtString = workingTimeArr[i];
					String[] split = wtString.split("-");
					
					String start = split[0];//9:00
					String end   = split[1];//12:00
					
					String[] startArr = start.split(":");//[9,00]
					String[] endArr   = end.split(":");//[12,00]
					
					
					WorkTimeInterval workTimeInterval=new WorkTimeInterval();
					workTimeInterval.setRecruitmentInfoId(id);
					workTimeInterval.setStartHour(Integer.parseInt(startArr[0]));
					workTimeInterval.setStartMinute(Integer.parseInt(startArr[1]));
					
					workTimeInterval.setEndHour(Integer.parseInt(endArr[0]));
					workTimeInterval.setEndMinute(Integer.parseInt(endArr[1]));
					workTimeInterval.setSort(i);
					
					
					
					workTimeIntervalService.insert(workTimeInterval);
					
				}
			}
			
			json.setData("发布成功"); 
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
	
	/**
	 * 删除招聘
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/deleteRecruit.do", method = RequestMethod.POST)
    @ResponseBody
	public Json deleteRecruit(HttpServletRequest request, HttpServletResponse response, String token, String recruitId) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="删除招聘,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		
		if(null==token || "".equals(token)) {
			json.setSattusCode(StatusCode.PARAMS_NO_NULL);
			json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
			return json;
		}
		
		
		//token验证
		boolean validateToken = TokenUtil.validateToken(token, redisBean);
		if(!validateToken) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
				
		try {	
			
			String userId=redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==userId) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			if(null==recruitId || "".equals(recruitId)) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(recruitId)");
				return json;
			}
			
			RecruitmentInfo recruitmentInfo=new RecruitmentInfo();
			recruitmentInfo.setId(Integer.valueOf(recruitId));
			recruitmentInfo.setUpdateby(userId);
			recruitmentInfo.setUpdatetime(new Date());
			recruitmentInfo.setActive(1);//是否有效 0:有效 1:无效
			recruitmentService.update(recruitmentInfo);
			json.setSuc();
			json.setData("删除招聘成功");
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",list"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	
	/**
	 * 重新上架招聘信息
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/dercarriage.do", method = RequestMethod.POST)
    @ResponseBody
	public Json dercarriage(HttpServletRequest request, HttpServletResponse response, String token, String recruitId) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="重新上架招聘信息,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		
		if(null==token || "".equals(token)) {
			json.setSattusCode(StatusCode.PARAMS_NO_NULL);
			json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
			return json;
		}
		
		
		//token验证
		boolean validateToken = TokenUtil.validateToken(token, redisBean);
		if(!validateToken) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
				
		try {	
			
			String userId=redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==userId) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			if(null==recruitId || "".equals(recruitId)) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(recruitId)");
				return json;
			}
			
			RecruitmentInfo recruitmentInfo=new RecruitmentInfo();
			recruitmentInfo.setId(Integer.valueOf(recruitId));
			recruitmentInfo.setCreateby(userId);
			recruitmentInfo.setCreatetime(new Date());
			recruitmentInfo.setUpdateby(userId);
			recruitmentInfo.setUpdatetime(new Date());
			recruitmentInfo.setState(Constant.RECRUITMENTINFO_RUNNING);//上架
			recruitmentService.update(recruitmentInfo);
			json.setSuc();
			json.setData("上架成功");
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",list"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	
	/**
	 * 下架招聘
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/undercarriage.do", method = RequestMethod.POST)
    @ResponseBody
	public Json undercarriage(HttpServletRequest request, HttpServletResponse response, String token, String recruitId) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="下架招聘,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		
		if(null==token || "".equals(token)) {
			json.setSattusCode(StatusCode.PARAMS_NO_NULL);
			json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
			return json;
		}
		
		
		//token验证
		boolean validateToken = TokenUtil.validateToken(token, redisBean);
		if(!validateToken) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
				
		try {	
			
			String userId=redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==userId) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			if(null==recruitId || "".equals(recruitId)) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(recruitId)");
				return json;
			}
			
			RecruitmentInfo recruitmentInfo=new RecruitmentInfo();
			recruitmentInfo.setId(Integer.valueOf(recruitId));
			recruitmentInfo.setUpdateby(userId);
			recruitmentInfo.setUpdatetime(new Date());
			recruitmentInfo.setState(Constant.RECRUITMENTINFO_DOWN);
			recruitmentService.update(recruitmentInfo);
			json.setSuc();
			json.setData("下架成功");
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",list"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	/**
	 * 快速改价
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/alterBasePay.do", method = RequestMethod.POST)
    @ResponseBody
	public Json alterBasePay(HttpServletRequest request, HttpServletResponse response, String token,
                             String recruitId, String basePay, String basePayUnit) {
		
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="快速改价,"+uuid;
		logger.info(title+",alterBasePay"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			if(null==token || "".equals(token)) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
				return json;
			}
			if(null==recruitId || "".equals(recruitId)) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(recruitId)");
				return json;
			}
			if(null==basePay || "".equals(basePay)) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(basePay)");
				return json;
			}
			if(null==basePayUnit || "".equals(basePayUnit)) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(basePayUnit)");
				return json;
			}
			
			if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			String userId=redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==userId) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			RecruitmentInfo recruitmentInfo=new RecruitmentInfo();
			recruitmentInfo.setId(Integer.valueOf(recruitId));
			recruitmentInfo.setUpdateby(userId);
			recruitmentInfo.setUpdatetime(new Date());
			recruitmentInfo.setBasePay(Float.parseFloat(basePay));
			recruitmentInfo.setBasePayUnit(basePayUnit);
			recruitmentService.update(recruitmentInfo);
			json.setSuc();
			json.setData("改价成功");
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",alterBasePay"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
		
	}
	
	/**
	 * 修改招聘信息-兼职
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/alterPartTime.do", method = RequestMethod.POST)
    @ResponseBody
	public Json alterPartTime(HttpServletRequest request, HttpServletResponse response,
                              RecruitmentInfo recruitmentInfo, String token, String sign, String timeStamp) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="修改招聘信息-兼职,"+uuid;
		logger.info(title+",alterPartTime"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			if(null==token || "".equals(token)) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
				return json;
			}
			
			if(null==recruitmentInfo.getId()) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(id)");
				return json;
			}
			
			if(null==recruitmentInfo.getDistrict()) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(district)");
				return json;
			}
			
			if(null==recruitmentInfo.getRecruitmentCategory()) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(recruitmentCategory)");
				return json;
			}
			
			if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			String userId=redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==userId) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			RecruitmentInfo infoOld = recruitmentService.findById(Long.valueOf(recruitmentInfo.getId()));
			//更新工作时间
			if(!StringUtils.isEmpty(recruitmentInfo.getWorkingStartTime()) && !StringUtils.isEmpty(recruitmentInfo.getWorkingEndTime())){
				if(!infoOld.getWorkingEndTime().equals(recruitmentInfo.getWorkingEndTime()) || !infoOld.getWorkingStartTime().equals(recruitmentInfo.getWorkingStartTime())){

					//先删除
					WorkTime oldWorkTime = new WorkTime();
					oldWorkTime.setRecruitmentInfoId(infoOld.getId());
					workTimeService.delete(oldWorkTime);
					logger.info("删除工作时间");
					//拆分工作时间 工作开始时间 如果有多条以英文逗号隔开 如：2019-06-13,2019-06-17
					String[] workingStartTimeArr = recruitmentInfo.getWorkingStartTime().split(",");
					String[] workingEndTimeArr = recruitmentInfo.getWorkingEndTime().split(",");

					recruitmentInfo.setState(Constant.RECRUITMENTINFO_RUNNING);
					recruitmentInfo.setActive(0);
					recruitmentService.add(recruitmentInfo);
					logger.info("更新工作时间");
					Integer id = recruitmentInfo.getId();

					for(int i=0;i<workingStartTimeArr.length;i++) {
						//工作时间
						WorkTime workTime=new  WorkTime();
						workTime.setRecruitmentInfoId(id);
						workTime.setStartTime(DateUtil.parase(workingStartTimeArr[i], "yyyy-MM-dd"));
						workTime.setEndTime(DateUtil.parase(workingEndTimeArr[i], "yyyy-MM-dd"));
						workTime.setSort(i);
						workTimeService.insert(workTime);
					}

				}
			}
			//更新工作小时
			if(!StringUtils.isEmpty(recruitmentInfo.getWorkingTime())){
				if(!infoOld.getWorkingTime().equals(recruitmentInfo.getWorkingTime())){

					//先删除旧的
					WorkTimeInterval interval = new WorkTimeInterval();
					interval.setRecruitmentInfoId(recruitmentInfo.getId());
					workTimeIntervalService.delete(interval);
					//上班时段 如果有多条以英文逗号隔开 如：9:00-12:00,2:00-6:00
					String[] workingTimeArr = recruitmentInfo.getWorkingTime().split(",");
					//工作时段 如果有多条以英文逗号隔开 如：9:00-12:00,2:00-6:00
					for(int i=0;i<workingTimeArr.length;i++) {
						String wtString = workingTimeArr[i];
						String[] split = wtString.split("-");
						String start = split[0];//9:00
						String end   = split[1];//12:00
						String[] startArr = start.split(":");//[9,00]
						String[] endArr   = end.split(":");//[12,00]
						WorkTimeInterval workTimeInterval=new WorkTimeInterval();
						workTimeInterval.setRecruitmentInfoId(recruitmentInfo.getId());
						workTimeInterval.setStartHour(Integer.parseInt(startArr[0]));
						workTimeInterval.setStartMinute(Integer.parseInt(startArr[1]));
						workTimeInterval.setEndHour(Integer.parseInt(endArr[0]));
						workTimeInterval.setEndMinute(Integer.parseInt(endArr[1]));
						workTimeInterval.setSort(i);
						workTimeIntervalService.insert(workTimeInterval);

					}
				}
			}



			
			recruitmentInfo.setUpdateby(userId);
			recruitmentInfo.setUpdatetime(new Date());
			recruitmentService.update(recruitmentInfo);
			json.setSuc();
			json.setData("修改成功");
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",alterPartTime"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}

}
