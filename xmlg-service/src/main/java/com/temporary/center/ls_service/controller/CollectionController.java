package com.temporary.center.ls_service.controller;

import com.github.pagehelper.PageHelper;
import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.*;
import com.temporary.center.ls_service.dao.CollectionMapper;
import com.temporary.center.ls_service.dao.RecruitmentInfoMapper;
import com.temporary.center.ls_service.domain.MyCollection;
import com.temporary.center.ls_service.domain.RecruitmentInfo;
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
	private CollectionMapper collectionService;
	@Autowired
	private RecruitmentInfoMapper recruitmentInfoService;
	
	/**
	 * 添加收藏
	 * @param recruitmentid
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/collection.do", method = RequestMethod.POST)
    @ResponseBody
	public Json collection(Long recruitmentid, String token) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="收藏职位,"+uuid;
		logger.info(title+",cityList"+Constant.METHOD_BEGIN);
		Json json = new Json();
		if(!TokenUtil.validateToken(token,redisBean)) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}

		String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
		if(null==userId || userId.equals("")) {
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		MyCollection collection = new MyCollection();
		collection.setCreate_by(Long.valueOf(userId));
		int cnt = collectionService.selectCount(collection);
		if(cnt>0){
			logger.info("已收藏");
			json.setSuc();
			return json;
		}
		RecruitmentInfo info = recruitmentInfoService.selectByPrimaryKey(recruitmentid);
		MyCollection myCollection = new MyCollection();
		myCollection.setRecruitment_id(recruitmentid);
		myCollection.setBase_pay(info.getBasePay());
		myCollection.setBase_pay_unit(info.getBasePayUnit());
		myCollection.setDistrict(info.getDistrict());
		myCollection.setTitle(info.getTitle());

		try {
			//判断token

			//判断参数
			json=ValidateParam.validateParamNotNull(myCollection);
			if(!json.getCode().equals(StatusCode.SUC.getCode())) {
				return json;
			}
 			myCollection.setCreate_by(Long.parseLong(userId));
 			myCollection.setCreate_time(new Date());
 			
			collectionService.insert(myCollection);
			
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
	 * @param curr
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/myCollection.do", method = RequestMethod.POST)
    @ResponseBody
	public Json myCollection(Integer curr,Integer pageSize, String token) {
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
			String createbystring = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==createbystring) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			MyCollection myCollection = new MyCollection();
			myCollection.setCreate_by(Long.parseLong(createbystring));
			PageHelper.startPage(curr,pageSize);
			int count=collectionService.selectCount(myCollection);


			List<MyCollection> list=collectionService.select(myCollection);
			PageData pageDate = new PageData(list,count,curr,pageSize);
			/*for(MyCollection collection2:list) {
				String basepayunit = collection2.getBasepayunit();
				if(null!=basepayunit && null!=Dictionary.wagesUnit.get(Integer.parseInt(basepayunit))) {
					collection2.setBasepayunit(Dictionary.wagesUnit.get(Integer.parseInt(basepayunit)));
				}
			}*/

//			Map<String, Object> result=new HashMap<>();
//			result.put("list", list);
//			result.put("count", count);
			json.setData(pageDate);
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
