package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.dao.CityMapper;
import com.temporary.center.ls_service.domain.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

	private static final Logger logger = LoggerFactory.getLogger(ProvinceCityController.class);

	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private CityMapper cityService;
	
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
		try {{
				List<City> cityList=cityService.selectAll();
				Collections.sort(cityList, new City());
				json.setData(cityList);
				json.setSuc();
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
