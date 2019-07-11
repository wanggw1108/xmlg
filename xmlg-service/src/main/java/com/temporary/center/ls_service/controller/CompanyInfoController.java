package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.ValidateParam;
import com.temporary.center.ls_service.domain.CompanyInfo;
import com.temporary.center.ls_service.domain.User;
import com.temporary.center.ls_service.params.CompanyInfoParam;
import com.temporary.center.ls_service.service.CompanyInfoService;
import com.temporary.center.ls_service.service.LogUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/companyInfo")
public class CompanyInfoController {

	private static final LogUtil logger = LogUtil.getLogUtil(ProvinceCityController.class);

	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private CompanyInfoService companyInfoService;
	
	@Autowired
	private LogUserService logUserService;
	
	/**
	 * 查询公司简介
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryCompanyInfo.do", method = RequestMethod.GET)
    @ResponseBody
    public Json queryCompanyInfo(HttpServletRequest request,
                                 HttpServletResponse response, CompanyInfo companyInfo, String token) {
		String title="查询公司简介,"+UUID.randomUUID().toString();
		logger.info(title+",queryCompanyInfo"+Constant.METHOD_BEGIN);
		Json json=new Json();
		try {
			//检测token 
			if(null==token || token.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
				return json;
			}
			String userId=null;
			if(!redisBean.exists(token)) {
				logger.info("token过期");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			userId=redisBean.get(token);
			
			if(null==userId || userId.equals("")) {
				logger.info("userId 为空redis");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			companyInfo.setCreateBy(Long.parseLong(userId));
			List<CompanyInfo> companyInfoList=companyInfoService.findByParam(companyInfo);
			
			if(null!=companyInfoList && companyInfoList.size()==1) {
				CompanyInfo companyInfo2 = companyInfoList.get(0);
				User userById = logUserService.getUserById(Long.parseLong(userId));
				//设置雇主信誉
				if(null!=userById && null!=userById.getBossReputation()) {
					companyInfo2.setEmployerReputation(userById.getBossReputation().toString());
				}else {
					companyInfo2.setEmployerReputation("0");
				}
				
				if(null==companyInfo2.getCompanyIsAuth()) {
					companyInfo2.setCompanyIsAuth(1);//公司是否做过企业认证 0:已认证；1：未认证
				}
				
				json.setData(companyInfo2);
				json.setSuc();
			}else if(null==companyInfoList || companyInfoList.size()==0){
				json.setSattusCode(StatusCode.NO_DATA);
				json.setData(StatusCode.NO_DATA.getMessage());
			}
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",queryCompanyInfo"+Constant.METHOD_END);
		return json;
	}
	
	/**
	 * 查询公司资质，主要是营业执照，其他。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qualifications.do", method = RequestMethod.GET)
    @ResponseBody
    public Json qualifications(HttpServletRequest request,
                               HttpServletResponse response, String token, String companyId) {
		String title="查询公司资质，主要是营业执照，其他。,"+UUID.randomUUID().toString();
		logger.info(title+",qualifications"+Constant.METHOD_BEGIN);
		Json json=new Json();
		try {
			//检测token 
			if(null==token || token.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
				return json;
			}
			if(null==companyId || companyId.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(companyId)");
				return json;
			}
			if(!redisBean.exists(token)) {
				logger.info("token过期");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			CompanyInfo  param=new CompanyInfo();
			param.setCompanyId(Long.parseLong(companyId));
			CompanyInfo companyInfo=companyInfoService.qualifications(param);
			json.setData(companyInfo);
			json.setSuc();
			
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",qualifications"+Constant.METHOD_END);
		return json;
	}
	
	
	/**
	 * 编辑公司简介
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/alterCompanyInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Json alterCompanyInfo(HttpServletRequest request,
                                 HttpServletResponse response, CompanyInfo companyInfo, String token) {
		String title="编辑公司简介,"+UUID.randomUUID().toString();
		logger.info(title+",alterCompanyInfo"+Constant.METHOD_BEGIN);
		Json json=new Json();
		try {
			//检测token 
			if(null==token || token.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
				return json;
			}
			String userId=null;
			if(!redisBean.exists(token)) {
				logger.info("token过期");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			userId=redisBean.get(token);
			
			if(null==userId || userId.equals("")) {
				logger.info("userId 为空redis");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			json=ValidateParam.validateParamNotNull(companyInfo);
			if(!json.getCode().equals(StatusCode.SUC.getCode())) {
				return json;
			}
			
			if(null!=companyInfo.getCompanyId()) {
				//编辑
				companyInfo.setUpdateBy(Long.parseLong(userId));
				companyInfo.setUpdateTime(new Date());
				json=update(companyInfo);
				
			}else {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(companyId)");
				return json;
			}
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",alterCompanyInfo"+Constant.METHOD_END);
		return json;
	}

	/**
	 * 编辑
	 * @param companyInfo
	 * @return
	 */
	private Json update(CompanyInfo companyInfo) {
		Json json=new Json();
		json.setSuc();
		json.setData("编辑成功");
		companyInfoService.update(companyInfo);
		return json;
	}

	
	/**
	 * 填写认证信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/createAuthInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Json createAuthInfo(HttpServletRequest request,
                               HttpServletResponse response, CompanyInfoParam companyInfo, String token) {
		String title="填写认证信息,"+UUID.randomUUID().toString();
		logger.info(title+",createAuthInfo"+Constant.METHOD_BEGIN);
		Json json=new Json();
		try {
			//检测token 
			if(null==token || token.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
				return json;
			}
			String userId=null;
			if(!redisBean.exists(token)) {
				logger.info("token过期");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			userId=redisBean.get(token);
			
			if(null==userId || userId.equals("")) {
				logger.info("userId 为空redis");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			json=ValidateParam.validateParamNotNull(companyInfo);
			if(!json.getCode().equals(StatusCode.SUC.getCode())) {
				return json;
			}
			//1：参数添加一个字段 authType(认证类型，1：公司，2：个人团队)
			//公司类型 0：公司；1：团队
			//2：如果authType=2时，营业执照、社会信用代码为非必传。
			if(companyInfo.getCompanyType().equals("0")) {
				//1：公司 验证 营业执照 社会信用代码
				if(null==companyInfo.getFileName() || "".equals(companyInfo.getFileName())) {
					json.setSattusCode(StatusCode.PARAMS_NO_NULL);
					json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(fileName)");
					return json;
				}
				if(null==companyInfo.getCompanyCode() || "".equals(companyInfo.getCompanyCode())) {
					json.setSattusCode(StatusCode.PARAMS_NO_NULL);
					json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(companyCode)");
					return json;
				}
			}
			
			//新增
			companyInfo.setCreateBy(Long.parseLong(userId));
			companyInfo.setCreateTime(new Date());
			
			CompanyInfo companyInfoadd=new CompanyInfo();
			
			BeanUtils.copyProperties(companyInfo, companyInfoadd);
			companyInfoadd.setCompanyIsAuth(2);//
			//用户的认证状态 1：未认证；2：审核中；3：已认证；4：审核不通过
			
			//查询自己是否创建过公司信息
			CompanyInfo isCreateParam=new CompanyInfo();
			isCreateParam.setCreateBy(Long.parseLong(userId));
			List<CompanyInfo> findByParam = companyInfoService.findByParam(isCreateParam);
			if(null!=findByParam && findByParam.size()>0) {
				json.setSattusCode(StatusCode.EXIST_DATA);
				json.setData("已存在公司信息");
				return json;
			}
			
			json=create(companyInfoadd);
			 
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info(title+",createAuthInfo"+Constant.METHOD_END);
		return json;
	}
	
	/**
	 * 新增
	 * @param companyInfo
	 * @return
	 */
	private Json create(CompanyInfo companyInfo) {
		Json json=new Json();
		json.setSuc();
		json.setData("新增成功");
		companyInfoService.add(companyInfo);
		return json;
	}
	
	
	
}
