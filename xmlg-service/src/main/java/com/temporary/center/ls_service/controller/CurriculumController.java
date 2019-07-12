package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.TokenUtil;
import com.temporary.center.ls_service.domain.CurriculumVitae;
import com.temporary.center.ls_service.domain.EducationExperience;
import com.temporary.center.ls_service.domain.ProjectExperience;
import com.temporary.center.ls_service.domain.WorkExperience;
import com.temporary.center.ls_service.params.CurriculumParam;
import com.temporary.center.ls_service.service.CurriculumService;
import com.temporary.center.ls_service.service.LogUserService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 简历
 * @author  fuyuanming
 *
 */
@Controller	
@RequestMapping(value = "/curriculum")
public class CurriculumController {
	
	private static final Logger logger = LoggerFactory.getLogger(CurriculumController.class);
	
	@Autowired
	private CurriculumService curriculumService;
	
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
			CurriculumVitae curriculumVitae=new CurriculumVitae();
			
//			dictionaries.setType(Constant.PART_TIME_TYPE);
			
			List<CurriculumVitae> list= curriculumService.list(curriculumVitae);
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
	
	/**
	 * 提交简历信息
	 * @param request
	 * @param response
	 * @param token     
	 * @param sign      签名
	 * @param timeStamp 时间戳
	 * @return
	 */
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
	public Json add(HttpServletRequest request, HttpServletResponse response,
                    String token, String sign, String timeStamp, String jsonStr) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="提交团队资料,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		 
		//String json 转  对象
		Json json=new Json ();
		
		//token验证 
		boolean validateToken = TokenUtil.validateToken(token, redisBean);
		if(!validateToken) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		
		try {
			
			JSONObject fromObject2 = JSONObject.fromObject(jsonStr);
			
			CurriculumParam curriculumParam2=(CurriculumParam)JSONObject.toBean(fromObject2, CurriculumParam.class);
			
			List<ProjectExperience> projectList = curriculumParam2.getProjectList();
			List<WorkExperience> workList = curriculumParam2.getWorkList();
			List<EducationExperience> educationList = curriculumParam2.getEducationList();
			
			
			
			Map<String,String> allParamsMap=new HashMap<String,String>();
			allParamsMap.put("token", token);
			allParamsMap.put("timeStamp", timeStamp);
			
			
			
			
			/*allParamsMap.put("name", name);
			allParamsMap.put("industry", industry);
			allParamsMap.put("scale", scale);
			allParamsMap.put("introduce", introduce);
			allParamsMap.put("address", address);
			allParamsMap.put("longitude", longitude);
			allParamsMap.put("latitude", latitude);
			allParamsMap.put("type", type);*/
			CurriculumVitae curriculumVitae=new CurriculumVitae(); 
			
			/*teamData.setName(name);
			teamData.setIndustry(industry);
			teamData.setScale(scale);
			teamData.setIndustry(industry);
			teamData.setAddress(address);
			teamData.setLongitude(longitude);
			teamData.setLatitude(latitude);
			teamData.setType(type);*/
			
 			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
 			if(null==userId) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			
 			/*teamData.setCreateby(userId);
 			teamData.setCreatetime(new Date());*/
			
 			String createSign = LoginController.createSign(allParamsMap, false);
			
			if(!createSign.equals(sign)) {
				json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
			}
			/*teamData.setStatus(Constant.IN_AUDIT);*/
			
			/*BeanUtils.copyProperties(curriculumParam, curriculumVitae);
			
			List<EducationExperience> educationList = curriculumParam.getEducationList();
			
			List<ProjectExperience> projectList = curriculumParam.getProjectList();
			
			List<WorkExperience> workList = curriculumParam.getWorkList();*/
			
			
			
			curriculumService.add(curriculumVitae);
			
			
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
	
	public static void main(String[] args) {
		
		CurriculumParam curriculumParam=new CurriculumParam();
		curriculumParam.setAge(22);
		
		List<EducationExperience> educationList=new ArrayList<EducationExperience>();
		EducationExperience educationExperience=new EducationExperience();
		educationExperience.setSchoolName("SchoolName222");
		educationList.add(educationExperience);
		curriculumParam.setEducationList(educationList);
		
		List<ProjectExperience> projectList=new ArrayList<ProjectExperience>();
		ProjectExperience projectExperience=new ProjectExperience();
		projectExperience.setName("fdsafds");
		projectList.add(projectExperience);
		curriculumParam.setProjectList(projectList);
		List<WorkExperience> workList=new ArrayList<WorkExperience>();
		WorkExperience workExperience=new WorkExperience();
		
		workExperience.setCorporateName("fdsa");
		workList.add(workExperience);
		curriculumParam.setWorkList(workList);
		
		JSONObject fromObject = JSONObject.fromObject(curriculumParam);
		
		System.out.println(fromObject.toString());
		
		
		String jsonStr="{\"additional\":\"\",\"age\":22,\"city\":\"\",\"createby\":0,\"createtime\":null,\"currentCompany\":\"\",\"currentPosition\":\"\",\"educationList\":[{\"begin\":null,\"createby\":0,\"createtime\":null,\"curriculumViteaId\":0,\"cv\":\"\",\"end\":null,\"id\":0,\"isUnified\":0,\"majorName\":\"\",\"schoolName\":\"SchoolName222\",\"updateby\":0,\"updatetime\":null}],\"email\":\"\",\"expectCity\":\"\",\"expectIndustry\":\"\",\"expectPosition\":\"\",\"expectSalary\":\"\",\"expectTime\":\"\",\"expectType\":\"\",\"fullTime\":\"\",\"height\":0,\"householdRegister\":\"\",\"id\":0,\"language\":\"\",\"name\":\"\",\"nationality\":\"\",\"okWorkTime\":\"\",\"projectList\":[{\"achievement\":\"\",\"begin\":null,\"createby\":0,\"createtime\":null,\"curriculumViteaId\":0,\"describe\":\"\",\"duty\":\"\",\"end\":null,\"id\":0,\"name\":\"fdsafds\",\"updateby\":0,\"updatetime\":null}],\"selfEvaluation\":\"\",\"status\":0,\"tel\":\"\",\"updateby\":0,\"updatetime\":null,\"workList\":[{\"begin\":null,\"companyIndustry\":\"\",\"companyNature\":\"\",\"companySize\":\"\",\"corporateName\":\"fdsa\",\"createby\":0,\"createtime\":null,\"curriculumViteaId\":0,\"dutyAchievement\":\"\",\"end\":\"\",\"id\":0,\"jobTitle\":\"\",\"reportingObject\":\"\",\"salary\":0,\"salaryUnit\":\"\",\"subordinatesNumber\":0,\"updateby\":0,\"updatetime\":null,\"workSpace\":\"\"}],\"workingLife\":\"\"}\r\n";
		
		JSONObject fromObject2 = JSONObject.fromObject(jsonStr);
		CurriculumParam curriculumParam2=(CurriculumParam)JSONObject.toBean(fromObject2, CurriculumParam.class);

		Integer age = curriculumParam2.getAge();
		System.out.println(age);
		
		
		
	}
	
}
