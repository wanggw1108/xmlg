package com.temporary.center.ls_service.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.*;
import com.temporary.center.ls_service.dao.*;
import com.temporary.center.ls_service.domain.*;
import com.temporary.center.ls_service.params.CurriculumParam;
import com.temporary.center.ls_service.service.LogUserService;
import com.temporary.center.ls_service.service.RecruitmentService;
import org.apache.http.client.utils.DateUtils;
import org.apache.solr.common.StringUtils;
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
 * 简历
 * @author  fuyuanming
 *
 */
@Controller	
@RequestMapping(value = "/curriculum")
public class CurriculumController {
	
	private static final Logger logger = LoggerFactory.getLogger(CurriculumController.class);
	
	@Autowired
	private CurriculumVitaeMapper curriculumService;
	@Autowired
	private EducationExperienceMapper educationExperienceService;
	@Autowired
	private WorkExperienceMapper workExperienceService;

	@Autowired
	ProjectExperienceMapper projectExperienceService;
	
	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private LogUserService logUserService;
    @Autowired
    private RecruitmentService recruitmentService;
	
	
	@RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
	public Json list(String cityName,String areaName,Integer ageMin,Integer ageMax,String sex,String expectPosition,String employeeReputation,Integer crr,Integer pageSize) {
		logger.info("搜索简历cityName {},areaName {},ageMin {},ageMax {},sex {},expectPosition {},employeeReputation {}",cityName,areaName,ageMin,ageMax,sex,expectPosition,employeeReputation);
		
		Json json=new Json ();
		try {

            int year = Integer.valueOf(DateUtils.formatDate(new Date(),"yyyy"));
            //实际年龄换算为出生年份
            if(ageMin ==null){
                ageMin = 0;
            }
            if(ageMax==null){
                ageMax=100;
            }
            int endYear = year - ageMin;
            int startYear = year - ageMax;
            int count = curriculumService.countByParams(cityName,areaName,startYear,endYear,sex,expectPosition,employeeReputation);
            if(count==0){
                PageData pageData = new PageData(new ArrayList<CurriculumView>(),0,crr,pageSize);
                json.setData(pageData);
                json.setSuc("未搜索到简历");
                return json;
            }
            PageHelper.startPage(crr,pageSize);
            List<CurriculumView> result = curriculumService.searchByParams(cityName,areaName,startYear,endYear,sex,expectPosition,employeeReputation);
            for(CurriculumView v:result){
                v.setEmployeeJobExperience(recruitmentService.getEmployeeByUserId(v.getUser_id()));
                v.setEmployeeServiceDistrict(recruitmentService.getServiceArea(v.getUser_id()));
            }
            PageData pageData = new PageData(result,count,crr,pageSize);
            json.setData(pageData);
            json.setSuc();
            return json;
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		return json;
	}
	
	/**
	 * 提交简历基本信息
	 * @return
	 */
	@RequestMapping(value = "/updateBase.do",method = RequestMethod.POST)
    @ResponseBody
	public Json updateBase(@RequestBody String body ) {
		//String json 转  对象
		Json json=new Json ();
		logger.info("更新简历:"+body);
		com.alibaba.fastjson.JSONObject params = JSON.parseObject(body);
		String token = params.getString("token");
		CurriculumVitae curriculum = params.toJavaObject(CurriculumVitae.class);
		if(!redisBean.exists(RedisKey.USER_TOKEN+token)){
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		String user_id = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
		curriculum.setCreate_by(Integer.valueOf(user_id));
		try {
			json= ValidateParam.validateParamNotNull(curriculum);

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if(!json.getCode().equals(StatusCode.SUC.getCode())) {
			return json;
		}
		curriculum.setUpdate_time(new Date());
		curriculum.setUpdate_by(Integer.valueOf(user_id));
		curriculumService.updateById(curriculum);
		json.setSuc();
		return json;
	}
	/**
	 * 添加工作经历
	 * @return
	 */
	@RequestMapping(value = "/addWork.do",method = RequestMethod.POST)
	@ResponseBody
	public Json updateEdu(@RequestBody String body ) {
		//String json 转  对象
		Json json=new Json ();
		logger.info("添加工作经历：");
		com.alibaba.fastjson.JSONObject params = JSON.parseObject(body);
		String token = params.getString("token");
		WorkExperience work = params.toJavaObject(WorkExperience.class);
		if(!redisBean.exists(RedisKey.USER_TOKEN+token)){
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		String user_id = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
		work.setCreate_by(Integer.valueOf(user_id));
		work.setCreate_time(new Date());
		try {
			json= ValidateParam.validateParamNotNull(work);

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if(!json.getCode().equals(StatusCode.SUC.getCode())) {
			return json;
		}
		workExperienceService.insert(work);
		CurriculumVitae vitae = curriculumService.selectByCreateBy(Integer.valueOf(user_id));
		vitae.setUpdate_time(new Date());
		curriculumService.updateById(vitae);
		json.setSuc();
		return json;
	}
	/**
	 * 添加教育经历
	 * @return
	 */
	@RequestMapping(value = "/addEdu.do",method = RequestMethod.POST)
	@ResponseBody
	public Json updateWork(@RequestBody String body) {
		//String json 转  对象
		com.alibaba.fastjson.JSONObject params = JSON.parseObject(body);
		String token = params.getString("token");
		EducationExperience edu = params.toJavaObject(EducationExperience.class);
		Json json=new Json ();
		if(!redisBean.exists(RedisKey.USER_TOKEN+token)){
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		String user_id = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
		edu.setCreate_by(Integer.valueOf(user_id));
		edu.setCreate_time(new Date());
		try {
			json= ValidateParam.validateParamNotNull(edu);

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if(!json.getCode().equals(StatusCode.SUC.getCode())) {
			return json;
		}
		educationExperienceService.insert(edu);
        CurriculumVitae vitae = curriculumService.selectByCreateBy(Integer.valueOf(user_id));
        vitae.setUpdate_time(new Date());
        curriculumService.updateById(vitae);
		json.setSuc();
		return json;
	}
	/**
	 * 查询自己简历
	 * @return
	 */
	@RequestMapping(value = "/query.do" ,method = RequestMethod.GET)
	@ResponseBody
	public Json query(String token ,Integer curriculum_id) {
		//String json 转  对象
		Json json=new Json ();
		if(!redisBean.exists(RedisKey.USER_TOKEN+token)){
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		int user_id = Integer.valueOf(redisBean.hget(RedisKey.USER_TOKEN+token,"user_id"));
        CurriculumVitae vitae = null;
		if(curriculum_id!=null){
            vitae = curriculumService.selectByPrimaryKey(curriculum_id);
        }else {
            vitae = curriculumService.selectByCreateBy(user_id);
        }
		if(vitae==null){
			json.setSattusCode(StatusCode.DATA_ERROR);
			return json;
		}
		CurriculumParam param = new CurriculumParam();
		param.setCurriculum(vitae);
		EducationExperience educationExperience = new EducationExperience();
		educationExperience.setCurriculum_vitea_id(vitae.getId());
		param.setEducationList(educationExperienceService.select(educationExperience));
		WorkExperience work = new WorkExperience();
		work.setCurriculum_vitea_id(vitae.getId());
		param.setWorkList(workExperienceService.select(work));
		ProjectExperience project = new ProjectExperience();
		project.setCurriculum_vitea_id(vitae.getId());
		param.setProjectList(projectExperienceService.select(project));
		json.setData(param);
		json.setSuc();

		return json;
	}


	
}
