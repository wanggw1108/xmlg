package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.*;
import com.temporary.center.ls_service.domain.*;
import com.temporary.center.ls_service.result.MyRecruitResult;
import com.temporary.center.ls_service.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	private WorkTimeService workTimeService;
	
	@Autowired
	private WorkTimeIntervalService workTimeIntervalService;
	
	@Autowired
	private JoinService joinService;
	
	
	
	
	
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
				
				/*if(null!=recruitmentInfo2.getBasePayUnit()) {
					String basePayUnitStr = PartTimeJobData.wagesUnit.get(Integer.parseInt(recruitmentInfo2.getBasePayUnit()));
					if(null!=basePayUnitStr) {
						myRecruitResult.setBasePayUnit(basePayUnitStr);//工资单位
					}else {
						logger.info("工资单位不存在BasePayUnit="+recruitmentInfo.getBasePayUnit());
					}
				}*/
				
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
				Long countByParam = joinService.countByParam(join1);
				myRecruitResult.setRecruitedNumber(countByParam.toString());//已经招聘的人数
				
				
				myRecruitResult.setRecruitId(recruitmentInfo2.getId().toString());//招聘信息的ID
				myRecruitResult.setReleaseTime(DateUtil.Date2DTstring(recruitmentInfo2.getCreatetime()));//发布时间
				
				Join join2=new Join();
				join2.setResumeId(recruitmentInfo2.getId());
				Long countByParam2 = joinService.countByParam(join2);
				myRecruitResult.setSignUpNumber(countByParam2.toString());//报名人数
				
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
			recruitmentInfo.setSort("createTime");
			recruitmentInfo.setActive(0);//
			
			List<RecruitmentInfo> list =recruitmentService.listIndex(recruitmentInfo);
			
			//设置雇员信誉和雇主信誉
			if(null!=list && list.size()>0) {
				for(RecruitmentInfo rInfo:list) {
					logger.info(uuid+"返回的记录:"+ClassUtil.printlnFieldValue(rInfo));
					
					
					/*String welfare = rInfo.getWelfare();
					StringBuffer welfareSb=new StringBuffer();
					
					if(null!=welfare && !"".equals(welfare)) {
						//福利翻译
						String[] welfareArr = welfare.split(",");
						for(String wel:welfareArr) {
							welfareSb.append(PartTimeJobData.welfare.get(Integer.parseInt(wel))+",");
						}
					}
					
					if(welfareSb.length()>0) {
						rInfo.setWelfare(welfareSb.substring(0, welfareSb.length()-1));
					}*/
					
					//获取浏览次数
					String key =RedisKey.RECRUITMENTINFO_COUNT+ rInfo.getId();
					Long browseTime=0L;
					if(redisBean.exists(key)) {
						browseTime=Long.parseLong(redisBean.get(key));
					}
					rInfo.setBrowseTime(browseTime);
					
					String createby = rInfo.getCreateby();
					if(null==createby || createby.equals("")) {
						logger.warn("createby 为空");
					}else {
						//员工和雇主的信誉
						String employeeAndBossReputation = redisBean.hget(RedisKey.USER_LIST, createby);
						String employee =null;
						String boss=null;
						String createSex =null;
						String userImageUrl =null;
						    if(null==employeeAndBossReputation) {
						    	User user = logUserService.getUserById(Long.parseLong(createby) );
						    	if(null==user) {
						    		employee="";
							    	boss="";
							    	createSex="";
							    	userImageUrl="";
						    	}else{
							    	if(null!=user.getEmployeeReputation()) {
							    		employee=user.getEmployeeReputation().toString();
							    	}
							    	if(null!=user.getBossReputation()) {
							    		boss=user.getBossReputation().toString();
							    	}
							    	if(null!=user.getSex()) {
							    		createSex=user.getSex().toString();	
							    	}
							    	userImageUrl=user.getUserImageUrl();
						    	}
						    	
						    }else {
						    	String[] employeeAndBossReputationArr = employeeAndBossReputation.split("#");
								if(null!=employeeAndBossReputationArr && employeeAndBossReputationArr.length==4) {
									 employee =StringUtil.nullString(employeeAndBossReputationArr[0]) ;
									 boss=StringUtil.nullString(employeeAndBossReputationArr[1]);
									 createSex =StringUtil.nullString(employeeAndBossReputationArr[2]);
									 userImageUrl =StringUtil.nullString(employeeAndBossReputationArr[3]);
								}else {
									logger.warn("员工和雇主的信誉 有问题，redis 值有误 key="+RedisKey.USER_LIST+","+createby+",value="+employeeAndBossReputation);
								}
						    }
						    	
						    rInfo.setCreateSex(createSex);//招聘人的性别 1是男；2是女
							rInfo.setUserImageUrl(userImageUrl);//用户头像
							if(null!=employee && !"".equals(employee) && !"null".equals(employee)) {
								rInfo.setEmployeeReputation(Integer.parseInt(employee));
							}
							if(null!=boss && !"".equals(boss) && !"null".equals(boss)) {
								rInfo.setBossReputation(Integer.parseInt(boss));
							}
					}
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
	 * 职位 详情
	 * @return
	 */
	@RequestMapping(value = "/listPage.do", method = RequestMethod.GET)
    @ResponseBody
	public Json listPage(Integer curr, Integer pageSize, RecruitmentInfo recruitmentInfo, HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="职位详情,"+uuid;
		logger.info(title+",recruitmentList"+Constant.METHOD_BEGIN);
		logger.info("入参");
		Json json=new Json ();
		try {
			
			//检测方法参数
			if(null==curr) {
				curr=1;
			}
			
			if(null==pageSize) {
				pageSize=10;
			}
			
			if(null==recruitmentInfo.getId()) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(id)");
				return json;
			}
			
			if(null!=recruitmentInfo && null!=recruitmentInfo.getId()) {
	            String key =RedisKey.RECRUITMENTINFO_COUNT+ recruitmentInfo.getId();
	            boolean exists = redisBean.exists(key);
	            if(!exists) {
	            	redisBean.incr(key);
	            }else {
	            	redisBean.incrBy(key, 1);
	            }
			}
			
			recruitmentInfo.setSort("createTime");
			recruitmentInfo.setSortRule(Constant.DESC);
			
//			Long count=recruitmentService.countByParams(recruitmentInfo);
			
//			if(null==count || count.equals(0L)) {
//				json.setSattusCode(StatusCode.NO_DATA);
//				return json;
//			}
			
			/*Long pageCount=0L;
			if(count%pageSize==0) {
				pageCount=count/pageSize;
			}else {
				pageCount=(count/pageSize)+1;
			}
			if(curr>pageCount) {
				curr=pageCount.intValue();
			}*/
			
			recruitmentInfo.setPageSize(pageSize);
			recruitmentInfo.setCurr(curr);
			
			
			List<RecruitmentInfo> list = recruitmentService.list(recruitmentInfo);
			//设置雇员信誉和雇主信誉
			if(null!=list && list.size()>0) {
				for(RecruitmentInfo rInfo:list) {
					String createby = rInfo.getCreateby();
					if(null==createby || createby.equals("")) {
						logger.warn("createby 为空");
					}else {
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
					}
				}
			}
			
//			PageData pageData=new PageData();
//			pageData.setCount(count);
//			pageData.setList(list);
			
			if(list.size()==1) {
				json.setData(list.get(0));
			}else {
				logger.info("数据库数据有误");
			}
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
	 * 发布兼职
	 */
	@Transactional
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
    public Json add(HttpServletRequest request, HttpServletResponse response,
                    String token, String sign, String timeStamp, RecruitmentInfo recruitmentInfo) {
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
 			Float hourlyWage = PartTimeJobData.switchUnit(recruitmentInfo.getBasePayUnit(), Float.parseFloat(recruitmentInfo.getBasePayUnit()));
 			recruitmentInfo.setHourlyWage(hourlyWage);
 			
 			/*String createSign = LoginController.createSign(allParamsMap, false);
 			if(SignUtil.isOpen()) {
 				if(!createSign.equals(sign)) {
 					logger.debug("签名="+createSign);
 					json.setSattusCode(StatusCode.SIGN_ERROR);
 					return json;
 				}
 			}*/
			
			recruitmentInfo.setActive(Integer.parseInt(Constant.EFFECTIVE) );
			recruitmentInfo.setRecruitment(0);//已经招聘人数 默认为0
			
			//拆分工作时间 工作开始时间 如果有多条以英文逗号隔开 如：2019-06-13,2019-06-17
			String[] workingStartTimeArr = recruitmentInfo.getWorkingStartTime().split(",");
			String[] workingEndTimeArr = recruitmentInfo.getWorkingEndTime().split(",");
			
			//添加时薪hourly_wage
			recruitmentInfo.setState(Constant.RECRUITMENTINFO_RUNNING);
			recruitmentInfo.setActive(0);
			recruitmentService.add(recruitmentInfo);
			
			Long id = recruitmentInfo.getId();
			
			for(int i=0;i<workingStartTimeArr.length;i++) {
				//工作时间
				WorkTime workTime=new  WorkTime();
				workTime.setRecruitmentInfoId(id);
				workTime.setStartTime(DateUtil.parase(workingStartTimeArr[i], "yyyy-MM-dd"));
				workTime.setEndTime(DateUtil.parase(workingEndTimeArr[i], "yyyy-MM-dd"));
				workTime.setSort(i);
				workTimeService.add(workTime);
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
					
					
					
					workTimeIntervalService.add(workTimeInterval);
					
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
			recruitmentInfo.setId(Long.parseLong(recruitId));
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
			recruitmentInfo.setId(Long.parseLong(recruitId));
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
			recruitmentInfo.setId(Long.parseLong(recruitId));
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
			recruitmentInfo.setId(Long.parseLong(recruitId));
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
			
	
	
	/**
	 * 修改兼职信息
	 */
	@Transactional
	@RequestMapping(value = "/update.do", method = RequestMethod.POST)
    @ResponseBody
	public Json update(HttpServletRequest request, HttpServletResponse response, String id,
                       String token, String sign, String timeStamp, String titleWork, String typeWork,
                       String number, String settlementMethod, String basePay, String type,
                       String sex, String welfare, String workingStartTime, String workingEndTime,
                       String workingTime, String workPlace, String longitude, String latitude, String detail
			, String recruitmentStartTime, String recruitmentEndTime, String contactsName, String tel, String email) {
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
			
			Map<String,String> allParamsMap=new HashMap<String,String>();
			allParamsMap.put("token", token);
			allParamsMap.put("timeStamp", timeStamp);
			allParamsMap.put("titleWork", titleWork);
			allParamsMap.put("typeWork", typeWork);
			allParamsMap.put("number", number);
			allParamsMap.put("settlementMethod", settlementMethod);
			allParamsMap.put("basePay", basePay);
			allParamsMap.put("type", type);
			allParamsMap.put("sex", sex);
			allParamsMap.put("welfare", welfare);
			allParamsMap.put("workingStartTime", workingStartTime);
			allParamsMap.put("workingEndTime", workingEndTime);
			allParamsMap.put("workingTime", workingTime);
			allParamsMap.put("workPlace", workPlace);
			allParamsMap.put("longitude", longitude);
			allParamsMap.put("latitude", latitude);
			allParamsMap.put("detail", detail);
			allParamsMap.put("recruitmentStartTime", recruitmentStartTime);
			allParamsMap.put("recruitmentEndTime", recruitmentEndTime);
			allParamsMap.put("contactsName", contactsName);
			allParamsMap.put("tel", tel);
			allParamsMap.put("email", email);
			
			RecruitmentInfo recruitmentInfo=new RecruitmentInfo(); 
			recruitmentInfo.setTitle(titleWork);
			recruitmentInfo.setTypeWork(typeWork);
			recruitmentInfo.setNumber(Integer.parseInt(number));
			recruitmentInfo.setSettlementMethod(Integer.parseInt(settlementMethod));
			recruitmentInfo.setBasePay(Float.parseFloat(basePay));
			recruitmentInfo.setType(Integer.parseInt(type));
			recruitmentInfo.setSex(Integer.parseInt(sex));
			recruitmentInfo.setWelfare(welfare);
			recruitmentInfo.setWorkingStartTime(workingStartTime);
			recruitmentInfo.setWorkingEndTime(workingEndTime);
			recruitmentInfo.setWorkingTime(workingTime);
			recruitmentInfo.setWorkPlace(workPlace);
			recruitmentInfo.setLongitude(longitude);
			recruitmentInfo.setLatitude(latitude);
			recruitmentInfo.setDetail(detail);
			
			recruitmentInfo.setContactsName(contactsName);
			recruitmentInfo.setTel(tel);
			recruitmentInfo.setEmail(email);
			
 			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
 			if(null==userId) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			
 			recruitmentInfo.setCreateby(userId);
 			recruitmentInfo.setCreatetime(new Date());
			
 			String createSign = LoginController.createSign(allParamsMap, false);
			
			if(!createSign.equals(sign)) {
				logger.debug("签名="+createSign);
				json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
			}
			recruitmentInfo.setActive(Integer.parseInt(Constant.EFFECTIVE) );
			recruitmentInfo.setRecruitment(0);//已经招聘人数 默认为0
			
			
			//拆分工作时间 工作开始时间 如果有多条以英文逗号隔开 如：2019-06-13,2019-06-17
			String[] workingStartTimeArr = workingStartTime.split(",");
			String[] workingEndTimeArr = workingEndTime.split(",");
			
			recruitmentService.add(recruitmentInfo);
			
//			Long id = recruitmentInfo.getId();
			
			
			
			
			for(int i=0;i<workingStartTimeArr.length;i++) {
				//工作时间
				WorkTime workTime=new  WorkTime();
//				workTime.setRecruitmentInfoId(id);
				workTime.setStartTime(DateUtil.parase(workingStartTimeArr[i], "yyyy-MM-dd"));
				workTime.setEndTime(DateUtil.parase(workingEndTimeArr[i], "yyyy-MM-dd"));
				workTime.setSort(i);
				workTimeService.add(workTime);
			}
			
			
			if(null!= workingTime && !"".equals(workingTime)) {
				//上班时段 如果有多条以英文逗号隔开 如：9:00-12:00,2:00-6:00
				String[] workingTimeArr = workingTime.split(",");
				//工作时段 如果有多条以英文逗号隔开 如：9:00-12:00,2:00-6:00
				for(int i=0;i<workingTimeArr.length;i++) {
					
					String wtString = workingTimeArr[i];
					String[] split = wtString.split("-");
					
					String start = split[0];//9:00
					String end   = split[1];//12:00
					
					String[] startArr = start.split(":");//[9,00]
					String[] endArr   = end.split(":");//[12,00]
					
					
					WorkTimeInterval workTimeInterval=new WorkTimeInterval();
//					workTimeInterval.setRecruitmentInfoId(id);
					workTimeInterval.setStartHour(Integer.parseInt(startArr[0]));
					workTimeInterval.setStartMinute(Integer.parseInt(startArr[1]));
					
					workTimeInterval.setEndHour(Integer.parseInt(endArr[0]));
					workTimeInterval.setEndMinute(Integer.parseInt(endArr[1]));
					workTimeInterval.setSort(i);
					workTimeIntervalService.add(workTimeInterval);
					
				}
			}
			
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
