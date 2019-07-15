package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.TokenUtil;
import com.temporary.center.ls_service.common.ValidateParam;
import com.temporary.center.ls_service.domain.MyCollection;
import com.temporary.center.ls_service.service.CollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
@RequestMapping(value = "/employee")
public class CollectionController {

	private static final Logger logger = LoggerFactory.getLogger(CollectionController.class);
	
	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private CollectionService collectionService;
	
	/**
	 * 添加收藏
	 * @param myCollection
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/collection.do", method = RequestMethod.POST)
    @ResponseBody
	public Json collection(MyCollection myCollection, String token) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="收藏职位,"+uuid;
		logger.info(title+",cityList"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			//判断token
			if(!TokenUtil.validateToken(token,redisBean)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			//判断参数
			json=ValidateParam.validateParamNotNull(myCollection);
 			if(!json.getCode().equals(StatusCode.SUC.getCode())) {
 				return json;
 			}
 			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
 			if(null==userId || userId.equals("")) {
 				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
 			}
 			myCollection.setCreate_by(Long.parseLong(userId));
 			myCollection.setCreate_time(new Date());
 			
			collectionService.add(myCollection);
			
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",cityList"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	/**
	 * 我的收藏
	 * @param myCollection
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/myCollection.do", method = RequestMethod.POST)
    @ResponseBody
	public Json myCollection(MyCollection myCollection, String token) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="我的收藏,"+uuid;
		logger.info(title+",myCollection"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			//判断token
			if(!TokenUtil.validateToken(token,redisBean)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			if(null==token || "".equals(token)) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(token)");
				return json;
			}
			
			if(null== myCollection.getCurr()) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(curr)");
				return json;
			}
			
			if(null== myCollection.getPageSize()) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"(pageSize)");
				return json;
			}
			
			
			String createbystring = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==createbystring) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			myCollection.setCreate_by(Long.parseLong(createbystring));
			
			Long count=collectionService.countByParam(myCollection);
			
			 
			List<MyCollection> list=collectionService.findByParam(myCollection);
			
			/*for(MyCollection collection2:list) {
				String basepayunit = collection2.getBasepayunit();
				if(null!=basepayunit && null!=PartTimeJobData.wagesUnit.get(Integer.parseInt(basepayunit))) {
					collection2.setBasepayunit(PartTimeJobData.wagesUnit.get(Integer.parseInt(basepayunit)));
				}
			}*/
			
			Map<String, Object> result=new HashMap<>();
			result.put("list", list);
			result.put("count", count);
			json.setData(result);
			json.setSuc();
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",cityList"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	
	
}
