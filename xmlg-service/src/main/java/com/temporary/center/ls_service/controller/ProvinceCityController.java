package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.domain.City;
import com.temporary.center.ls_service.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 省份城市
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/provinceCity")
public class ProvinceCityController {

	private static final LogUtil logger = LogUtil.getLogUtil(ProvinceCityController.class);

	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private CityService cityService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cityList.do", method = RequestMethod.POST)
    @ResponseBody
    public Json cityList(HttpServletRequest request,
            HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="获取城市列表,"+uuid;
		logger.info(title+",cityList"+Constant.METHOD_BEGIN);
		
		Json json=new Json();
		try {
			
			if(redisBean.exists(RedisKey.CITY_LIST)) {
				byte[] bs = redisBean.get(RedisKey.CITY_LIST.getBytes());
				ByteArrayInputStream bis = new ByteArrayInputStream(bs);
		        ObjectInputStream inputStream = new ObjectInputStream(bis);
		        List<City> cityList = (List<City>) inputStream.readObject();
		        json.setData(cityList);
				json.setSuc();
				inputStream.close();
			    bis.close();
			}else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(bos);
				List<City> cityList=cityService.getCityALL();
				Collections.sort(cityList, new City());
				json.setData(cityList);
				json.setSuc();
				oos.writeObject(cityList);
				byte[] byteArray = bos.toByteArray();
				redisBean.set(RedisKey.CITY_LIST.getBytes(), byteArray); 
				oos.close();
				bos.close();
			}
		}catch (Exception e) {
			 e.printStackTrace();
			 json.setFail();
		}
		
		logger.info(title+",cityList"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
		
	}
	
}
