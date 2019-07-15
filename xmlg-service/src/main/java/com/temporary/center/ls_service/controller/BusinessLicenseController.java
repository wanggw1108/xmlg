package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.TokenUtil;
import com.temporary.center.ls_service.dao.BusinessLicenseMapper;
import com.temporary.center.ls_service.domain.BusinessLicense;
import com.temporary.center.ls_service.domain.Dictionaries;
import com.temporary.center.ls_service.service.BusinessLicenseService;
import com.temporary.center.ls_service.service.LogUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * 营业执照
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/businessLicense")
public class BusinessLicenseController {

	private static final Logger logger = LoggerFactory.getLogger(BusinessLicenseController.class);

	@Autowired
	private BusinessLicenseMapper businessLicenseService;
	
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
		return json;
	}
	
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
	public Json add(HttpServletRequest request, HttpServletResponse response,
                    String token, String sign, String timeStamp, String name, String code) {
		Json json=new Json ();
		return json;
	}
	
	
	
}
