package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.TokenUtil;
import com.temporary.center.ls_service.dao.CompanyInfoMapper;
import com.temporary.center.ls_service.dao.IdCardMapper;
import com.temporary.center.ls_service.dao.TeamDataMapper;
import com.temporary.center.ls_service.domain.CompanyInfo;
import com.temporary.center.ls_service.domain.IdCard;
import com.temporary.center.ls_service.domain.TeamData;
import com.temporary.center.ls_service.service.CompanyInfoService;
import com.temporary.center.ls_service.service.LogUserService;
import com.temporary.center.ls_service.service.TeamDataService;
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
 * 团队资料
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/teamdata")
public class TeamDataController {

	private static final Logger logger = LoggerFactory.getLogger(TeamDataController.class);

	@Autowired
	private TeamDataMapper teamDataService;
	
	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private LogUserService logUserService;
	
	@Autowired
	private CompanyInfoMapper companyInfoService;

	@Autowired
	private IdCardMapper idCardMapper;
	
	
	@RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
	public Json list() {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="查询营业执照,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			TeamData businessLicense=new TeamData();
//			dictionaries.setType(Constant.PART_TIME_TYPE);
			
			List<TeamData> list= teamDataService.select(businessLicense);
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
	 * 用户是否认证
	 * @param token
	 * @param timeStamp
	 * @return
	 */
	@RequestMapping(value = "/status.do", method = RequestMethod.POST)
    @ResponseBody
	public Json status(String token,String timeStamp) {
		Json json=new Json();
		
		try {
			
			//token验证 
			boolean validateToken = TokenUtil.validateToken(token, redisBean);
			if(!validateToken) {
				logger.info("token不存在");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==userId) {
				logger.info("token不不存在，userId");
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			CompanyInfo companyInfo=new CompanyInfo();
			companyInfo.setCreateBy(Long.parseLong(userId));
			CompanyInfo company = companyInfoService.selectOne(companyInfo);
			int status=1;
			String reason = null;
			Map<String, Object> result=new HashMap<String, Object>();
							IdCard idCard = new IdCard();
				idCard.setCreateby(userId);
				idCard = idCardMapper.selectOne(idCard);
				if(idCard==null){
			logger.info("没有数据，未认证");
			result.put("status", "1");
			result.put("reason", "");
			json.setData(result);//
			json.setSuc();
			return json;
				}
			if(null==company) {

				//查看id_card表
//				IdCard idCard = new IdCard();
//				idCard.setCreateby(userId);
//				idCard = idCardMapper.selectOne(idCard);
//				if(idCard==null){
					logger.info("没有数据，未认证");
					result.put("status", "1");
					result.put("reason", "");
					json.setData(result);//
					json.setSuc();
					return json;
//				}
//				status = idCard.getStatus();
			}else {
				status = company.getCompanyIsAuth();
				reason = company.getReason();
			}
			result.put("status", status);
			if(null!=reason) {
				result.put("reason", reason);
			}
			json.setData(result);
			json.setSuc();
		} catch (Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		return json;
	}
	
	/**
	 * 提交团队资料
	 * @param request
	 * @param response
	 * @param token     
	 * @param sign      签名
	 * @param timeStamp 时间戳
	 * @return
	 */
	@RequestMapping(value = "/createAuthInfo.do", method = RequestMethod.POST)
    @ResponseBody
	public Json add(HttpServletRequest request, HttpServletResponse response,
                    String token, String sign, String timeStamp, TeamData teamData) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="提交团队资料,"+uuid;
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
 			teamData.setCreateBy(userId);
 			teamData.setCreatetime(new Date());
			teamData.setStatus(Constant.AUDIT_COMPLETED);
			teamDataService.insert(teamData);
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
