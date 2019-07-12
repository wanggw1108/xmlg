package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.TokenUtil;
import com.temporary.center.ls_service.domain.WalletDetail;
import com.temporary.center.ls_service.service.LogUserService;
import com.temporary.center.ls_service.service.WalletDetailService;
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
 * 我的钱包
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/walletDetail")
public class WalletDetailController {

	private static final Logger logger = LoggerFactory.getLogger(WalletDetailController.class);

	@Autowired
	private WalletDetailService walletDetailService;
	
	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private LogUserService logUserService;
	
	
	/**
	 * 查询钱包明细列表
	 * @param token
	 * @param sign
	 * @param timeStamp
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
	public Json list(String token,String sign,String timeStamp,  String page, String pageSize) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="查询钱包明细,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			if(null==page) {
				page="1";
			}
			
			if(null==pageSize) {
				pageSize="10";
			}
			
			if(null==token || token.equals("")) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			if(null==sign || sign.equals("")) {
				json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
			}
			
			//token验证
			boolean validateToken = TokenUtil.validateToken(token, redisBean);
			if(!validateToken) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			Long timeStampLong=Long.parseLong(timeStamp);
	        Date currDate=new Date();
	        long countTime=currDate.getTime()-timeStampLong;
	        if(countTime<0 || countTime>Constant.INTERFACE_TIME_OUT) {
	        	logger.info("时间相差countTime="+countTime);
	    		json.setSattusCode(StatusCode.TIME_OUT_FIVE_MINUTE);
	    		return json;
	        }
			
			
			
			Map<String,String> allParamsMap=new HashMap<String,String>();
			allParamsMap.put("token", token);
			allParamsMap.put("timeStamp", timeStamp);
			allParamsMap.put("page", page);
			allParamsMap.put("pageSize", pageSize);
			
			String createSign = LoginController.createSign(allParamsMap, false);
			if(!createSign.equals(sign)) {
				logger.debug("sign签名={0}", createSign);
				json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
			}
			
			WalletDetail walletDetail=new WalletDetail();
			
			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==userId) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			walletDetail.setUserid(Long.parseLong(userId) );
			walletDetail.setPageSize(Integer.parseInt(pageSize) );
			walletDetail.setCurr(Integer.parseInt(page));
			
			List<WalletDetail> list= walletDetailService.list(walletDetail);
			
			Long count=walletDetailService.count(walletDetail);
			
			Map<String,Object> resultMap=new HashMap<String,Object>();
			resultMap.put("count", count);
			resultMap.put("list",list);
			json.setData(resultMap);
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
	 * 我的钱包-当前金额
	 * @param token
	 * @param sign
	 * @param timeStamp
	 * @param random
	 * @return
	 */
	@RequestMapping(value = "/currAmount.do", method = RequestMethod.POST)
    @ResponseBody
	public Json currAmount(String token,String sign,String timeStamp,String random) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="查询当前金额,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			if(null==token || token.equals("")) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			if(null==sign || sign.equals("")) {
				json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
			}
			
			if(null==random || random.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(random)");
				return json;
			}
			
			//token验证
			boolean validateToken = TokenUtil.validateToken(token, redisBean);
			if(!validateToken) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			Long timeStampLong=Long.parseLong(timeStamp);
	        Date currDate=new Date();
	        long countTime=currDate.getTime()-timeStampLong;
	        if(countTime<0 || countTime>Constant.INTERFACE_TIME_OUT) {
	        	logger.info("时间相差countTime="+countTime);
	    		json.setSattusCode(StatusCode.TIME_OUT_FIVE_MINUTE);
	    		return json;
	        }
			
			
			
			Map<String,String> allParamsMap=new HashMap<String,String>();
			allParamsMap.put("token", token);
			allParamsMap.put("timeStamp", timeStamp);
			allParamsMap.put("random", random);
			String createSign = LoginController.createSign(allParamsMap, false);
			if(!createSign.equals(sign)) {
				logger.debug("sign签名={0}", createSign);
				json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
			}
			
			WalletDetail walletDetail=new WalletDetail();
			
			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==userId) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			walletDetail.setUserid(Long.parseLong(userId) );
			
			Double currAmount=walletDetailService.currAmount(walletDetail);
			 
			json.setData(currAmount);
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
	
	
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
	public Json add(HttpServletRequest request, HttpServletResponse response,
                    String token, String sign, String timeStamp,
                    String amount, String type, String remark) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="添加钱包明细,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		
		//token验证
		boolean validateToken = TokenUtil.validateToken(token, redisBean);
		if(!validateToken) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		
		if(null==amount || amount.equals("") || amount.equals("0")) {
			json.setSattusCode(StatusCode.AMOUNT_NULL);
			return json;
		}
		
		if(null==type || type.equals("")) {
			json.setSattusCode(StatusCode.TYPE_NULL);
			return json;
		}
		
		if(null==remark || remark.equals("")) {
			json.setSattusCode(StatusCode.REMARK_NULL);
			return json;
		}
		
		try {
			
			Map<String,String> allParamsMap=new HashMap<String,String>();
			allParamsMap.put("token", token);
			allParamsMap.put("timeStamp", timeStamp);
			allParamsMap.put("amount", amount);
			allParamsMap.put("type", type);
			allParamsMap.put("remark", remark);
			WalletDetail teamData=new WalletDetail(); 
			
			teamData.setAmount(Double.parseDouble(amount));
			teamData.setType(Integer.parseInt(type));
			teamData.setRemark(remark);
			
 			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
 			if(null==userId) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
 				return json;
 			}
 			
 			Long timeStampLong=Long.parseLong(timeStamp);
	        Date currDate=new Date();
	        long countTime=currDate.getTime()-timeStampLong;
	        if(countTime<0 || countTime>Constant.INTERFACE_TIME_OUT) {
	        	logger.info("时间相差countTime="+countTime);
	    		json.setSattusCode(StatusCode.TIME_OUT_FIVE_MINUTE);
	    		return json;
	        }
 			
	        teamData.setUserid(Long.parseLong(userId) );
 			teamData.setCreateby(userId);
 			teamData.setCreatetime(new Date());
			
 			String createSign = LoginController.createSign(allParamsMap, false);
			
			if(!createSign.equals(sign)) {
				logger.debug("sign签名={0}", createSign);
				json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
			}
			
			walletDetailService.add(teamData);
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
