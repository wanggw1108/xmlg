package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.DictionariesMapper;
import com.temporary.center.ls_service.domain.Dictionaries;
import com.temporary.center.ls_service.service.DictionariesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

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
	
	private static final Logger logger = LoggerFactory.getLogger(DictionariesController.class);

	@Autowired
	private DictionariesMapper dictionariesService;
	
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
			Example example = new Example(Dictionaries.class);
			Example.Criteria c = example.createCriteria();
			c.andEqualTo("type",type);
			example.setOrderByClause("sort asc");
			
			List<Dictionaries> list= dictionariesService.selectByExample(example);
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
