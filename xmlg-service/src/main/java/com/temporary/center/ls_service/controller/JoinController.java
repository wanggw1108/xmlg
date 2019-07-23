package com.temporary.center.ls_service.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.PageData;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.CurriculumVitaeMapper;
import com.temporary.center.ls_service.dao.JoinMapper;
import com.temporary.center.ls_service.domain.CurriculumVitae;
import com.temporary.center.ls_service.domain.Join;
import com.temporary.center.ls_service.domain.RecruitmentInfo;
import com.temporary.center.ls_service.domain.User;
import com.temporary.center.ls_service.result.JoinResult;
import com.temporary.center.ls_service.result.SignUpEmployeeInfo;
import com.temporary.center.ls_service.service.LogUserService;
import com.temporary.center.ls_service.service.RecruitmentService;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
	@Autowired
	ImageUtil imageUtil;
	@Autowired
	private CurriculumVitaeMapper curriculumVitaeService;
	
	
	@RequestMapping(value = "/seeSignUp.do", method = RequestMethod.GET)
    @ResponseBody
	public Json seeSignUp(String token,String recruitId,Integer curr,Integer pageSize) {
		
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
			if(null==pageSize) {
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
			joinResult.setBasePayUnit(recruitmentInfo.getBasePayUnit());
			String key =RedisKey.RECRUITMENTINFO_COUNT+ recruitmentInfo.getId();
			Long browseTime=0L;
			if(redisBean.exists(key)) {
				browseTime=Long.parseLong(redisBean.get(key));
			}
			joinResult.setBrowseNumber(browseTime.toString());//浏览次数
			joinResult.setReleaseTime(DateUtil.Date2DTstring(recruitmentInfo.getCreatetime()));//发布时间
			
			Join join=new Join();
			join.setRecruitmentInfoId(recruitmentInfo.getId());
			Integer countSignUpName=joinService.selectCount(join);
			joinResult.setSignUpNumber(countSignUpName.toString());//报名人数
			
			joinResult.setTitle(recruitmentInfo.getTitle());//招聘信息的标题
			String companyImg = (recruitmentInfo.getUserImageUrl()==null?imageUtil.getFileUrl(null,"4"):imageUtil.getFileUrl(recruitmentInfo.getCreateby()+"","4"));
			joinResult.setPortraitUrl(companyImg);
			//查询报名的雇员信息
			PageHelper.startPage(curr,pageSize);
			Join joinParam=new Join();
			joinParam.setRecruitmentInfoId(recruitmentInfo.getId());
			List<Join> joinList=joinService.select(joinParam);
			//装换
			List<SignUpEmployeeInfo> signUpEmployeeInfo =new ArrayList<>();
			int currentYear = Integer.valueOf(DateUtils.formatDate(new Date(),"yyyy"));
			for(Join join2:joinList) {
				Integer userId = join2.getUserId();
				User userById = logUserService.getUserById(userId);
				SignUpEmployeeInfo signUpEmployeeInfo2=new SignUpEmployeeInfo();
				CurriculumVitae vitae = curriculumVitaeService.selectByCreateBy(userId);
				if(vitae!=null){
					int age = vitae.getAge();
					signUpEmployeeInfo2.setEmployeeAge(currentYear - age);//雇员的年龄
				}
				//查询该雇员所参加过的职位信息
				signUpEmployeeInfo2.setEmployeeJobExperience(recruitmentService.getEmployeeByUserId(userId));//雇员的工作经历
				signUpEmployeeInfo2.setEmployeeName(userById.getChineseName());//雇员的姓名
				signUpEmployeeInfo2.setEmployeeportraitUrl(userById.getUserImageUrl());//雇员的头像URL
				signUpEmployeeInfo2.setEmployeeReputation(userById.getEmployeeReputation());//雇员的信誉
				signUpEmployeeInfo2.setEmployeeServiceDistrict(recruitmentService.getServiceArea(userId));//雇员的服务区域，多个服务区域以英文逗号隔开
				signUpEmployeeInfo2.setEmployeeSex(userById.getSex());//雇员的性别
				signUpEmployeeInfo2.setEmployeeId(join2.getId());
				signUpEmployeeInfo.add(signUpEmployeeInfo2);
			}
			joinResult.setSignUpEmployeeInfo(signUpEmployeeInfo);
			PageData pageData=new PageData(joinResult,countSignUpName,curr,pageSize);
			json.setData(pageData);
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
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/join.do", method = RequestMethod.POST)
    @ResponseBody
	public Json join(@RequestBody String params) {
		JSONObject jsonParams = JSONObject.parseObject(params);
		String recruitment_id = jsonParams.getString("recruitId");
		String token = jsonParams.getString("token");
		String remark = jsonParams.getString("remark");
		String startDate = jsonParams.getString("startDate");
		logger.info("报名 "+recruitment_id);
		Json json=new Json ();

		try {
			if(redisBean.exists(RedisKey.USER_TOKEN+token)) {
				String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
				Join join = new Join();
				Join joined = new Join();
				joined.setUserId(Integer.valueOf(userId));
				long num = joinService.selectCount(joined);
				//防止重复报名
				if(num>0){
					json.setSuc();
					return json;
				}
				User user=logUserService.getUserById(Long.parseLong(userId));
				join.setCreatetime(new Date());
				join.setUserId(Integer.parseInt(userId));//报名者id
				join.setUserName(user.getUserName());//报名者姓名
				join.setRecruitmentInfoId(Integer.valueOf(recruitment_id));//要报名的职位id
				RecruitmentInfo info = recruitmentService.findById(Long.valueOf(recruitment_id));
				join.setRecruitmentInfoCreateby(info.getCreateby());
				join.setRemark(remark);
				join.setStartDate(startDate);
				join.setState(1);//报名成功
				joinService.insert(join);
				json.setSuc();
				
			}else {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
			}
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		return json;
	}
	
}
