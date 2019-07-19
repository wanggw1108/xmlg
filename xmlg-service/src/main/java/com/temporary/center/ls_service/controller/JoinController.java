package com.temporary.center.ls_service.controller;

import com.github.pagehelper.PageHelper;
import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.JoinMapper;
import com.temporary.center.ls_service.domain.Join;
import com.temporary.center.ls_service.domain.RecruitmentInfo;
import com.temporary.center.ls_service.domain.User;
import com.temporary.center.ls_service.result.JoinResult;
import com.temporary.center.ls_service.result.SignUpEmployeeInfo;
import com.temporary.center.ls_service.service.JoinService;
import com.temporary.center.ls_service.service.LogUserService;
import com.temporary.center.ls_service.service.RecruitmentService;
import com.temporary.center.ls_service.service.impl.JoinServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 在线报名
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/joinController")
public class JoinController {

	private static final Logger logger = LoggerFactory.getLogger(JoinController.class);

	@Autowired
	private JoinMapper joinService;
	 
	@Autowired
	private RedisBean  redisBean;
	
	@Autowired
	private LogUserService logUserService;
	
	@Autowired
	private RecruitmentService recruitmentService;
	
	
	@RequestMapping(value = "/seeSignUp.do", method = RequestMethod.GET)
    @ResponseBody
	public Json seeSignUp(String token,String recruitId,Integer curr,Integer pagesize) {
		
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="查看报名,"+uuid;
		logger.info(title+",join"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			if(null==recruitId || recruitId.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(recruitId)");
				return json;
			}
			if(null==curr) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(curr)");
				return json;
			}
			if(null==pagesize) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(pagesize)");
				return json;
			}
			
			if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
				logger.info("token过期");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
			}
			
			JoinResult joinResult=new JoinResult();
			
			//设置职位信息
			RecruitmentInfo recruitmentInfo=recruitmentService.findById(Long.parseLong(recruitId));
			if(null==recruitmentInfo) {
				json.setSattusCode(StatusCode.NO_DATA);
				json.setData(StatusCode.NO_DATA.getMessage());
				return json;
			}
			
			
			joinResult.setBasePay(recruitmentInfo.getBasePay().intValue());//工资
			
			/*String basePayUnitStr = Dictionary.wagesUnit.get(Integer.parseInt(recruitmentInfo.getBasePayUnit()));
			if(null!=basePayUnitStr) {
				joinResult.setBasePayUnit(basePayUnitStr);//工资单位
			}else {
				logger.info("工资单位不存在BasePayUnit="+recruitmentInfo.getBasePayUnit());
			}*/
			
			String key =RedisKey.RECRUITMENTINFO_COUNT+ recruitmentInfo.getId();
			Long browseTime=0L;
			if(redisBean.exists(key)) {
				browseTime=Long.parseLong(redisBean.get(key));
			}
			joinResult.setBrowseNumber(browseTime.toString());//浏览次数
			joinResult.setReleaseTime(DateUtil.Date2DTstring(recruitmentInfo.getCreatetime()));//发布时间
			
			Join join=new Join();
			join.setResumeId(recruitmentInfo.getId());
			Integer countSignUpName=joinService.selectCount(join);
			joinResult.setSignUpNumber(countSignUpName.toString());//报名人数
			
			joinResult.setTitle(recruitmentInfo.getTitle());//招聘信息的标题
 			
			//查询创建的头像
			String createby = recruitmentInfo.getCreateby();
			User user=logUserService.getUserById(Long.parseLong(createby));
			if(user!=null) {
				String userImageUrl = user.getUserImageUrl();
				joinResult.setPortraitUrl(userImageUrl);
			}
			//查询报名的雇员信息
			PageHelper.startPage(curr,pagesize);
			Join joinParam=new Join();
			joinParam.setResumeId(recruitmentInfo.getId());
			joinParam.setPageSize(pagesize);
			joinParam.setCurr(curr);
			List<Join> joinList=joinService.select(joinParam);
			Map<String, Object> resultMap=new HashMap<>();
			
			//装换
			List<SignUpEmployeeInfo> signUpEmployeeInfo =new ArrayList<>(); 
			
			
			for(Join join2:joinList) {
				
				Integer userId = join2.getUserId();
				User userById = logUserService.getUserById(userId);
				
				
				SignUpEmployeeInfo signUpEmployeeInfo2=new SignUpEmployeeInfo();
				
				if(null!=userById.getBirthday()) {
					Date birthday = DateUtil.parase(userById.getBirthday(), "yy/MM/dd");
					int age = DateUtil.getAgeByBirth(birthday);
					signUpEmployeeInfo2.setEmployeeAge(age);//雇员的年龄
				}
				//TODO 
				signUpEmployeeInfo2.setEmployeeJobExperience("工作经历");//雇员的工作经历
				
				signUpEmployeeInfo2.setEmployeeName(join2.getUserName());//雇员的姓名
				signUpEmployeeInfo2.setEmployeeportraitUrl(userById.getUserImageUrl());//雇员的头像URL
				signUpEmployeeInfo2.setEmployeeReputation(userById.getEmployeeReputation());//雇员的信誉
				
				signUpEmployeeInfo2.setEmployeeServiceDistrict("服务区域A,服务区域B");//雇员的服务区域，多个服务区域以英文逗号隔开
				
				signUpEmployeeInfo2.setEmployeeSex(userById.getSex());//雇员的性别
				signUpEmployeeInfo2.setEmployeeId(join2.getId());
				signUpEmployeeInfo.add(signUpEmployeeInfo2);
			}
			
			joinResult.setSignUpEmployeeInfo(signUpEmployeeInfo);
			
			resultMap.put("list", joinResult);
			resultMap.put("count", countSignUpName);
			json.setData(resultMap);
			json.setSuc();
		}catch (Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",join"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		
		return json;
	}
	
	/**
	 * 立即报名
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/join.do", method = RequestMethod.POST)
    @ResponseBody
//	public Json join(String token,String sigin,String time,String resumeId,String remark) {
	public Json join(String token,Join join) {
	
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="立即报名,"+uuid;
		logger.info(title+",join"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {

			if(redisBean.exists(RedisKey.USER_TOKEN+token)) {
				
				String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
				User user=logUserService.getUserById(Long.parseLong(userId));
				join.setCreatetime(new Date());
				join.setUserId(Integer.parseInt(userId));
				join.setUserName(user.getUserName());
				joinService.insert(join);
				json.setSuc();
				
			}else {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
			}
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",join"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
}
