package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.domain.Dictionaries;
import com.temporary.center.ls_service.service.DictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

/**
 * 字典操作表
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/dictionaries")
public class DictionariesController {
	
	private static final LogUtil logger = LogUtil.getLogUtil(DictionariesController.class);

	@Autowired
	private DictionariesService dictionariesService;
	
	/**
	 * 
	 * @param type 1:兼职类型 2：所属行业 3：企业类型4：人员规模
	 * @return
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
    @ResponseBody
	public Json list(String type) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="查询字典,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			Dictionaries dictionaries=new Dictionaries();
			dictionaries.setType(Integer.parseInt(type));
			
			List<Dictionaries> list= dictionariesService.list(dictionaries);
			json.setData(list);
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
