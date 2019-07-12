package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 支付宝
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/zfb")
public class ZhiFuBaoController {

	private static final Logger logger = LoggerFactory.getLogger(ZhiFuBaoController.class);
	
	//网关
	@RequestMapping(value = "/gateway.do", method = RequestMethod.POST)
    @ResponseBody
    public void gateway(HttpServletRequest request,
            HttpServletResponse response) {
		logger.info("支付宝网关");
		
		Map parameterMap = request.getParameterMap();
		if(null==parameterMap) {
			return ;
		}
		logger.info(parameterMap.toString());
		Set entrySet = parameterMap.entrySet();
		
		Iterator iterator = entrySet.iterator();
		
		while(iterator.hasNext()) {
			Object next = iterator.next();
			logger.info(next.toString());
		}
		
	}
	
	
	//回调
	@RequestMapping(value = "/callback.do", method = RequestMethod.POST)
    @ResponseBody
    public void callback(HttpServletRequest request,
            HttpServletResponse response) {
		logger.info("支付宝回调");
		
		Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
            logger.info("return-param:{0}={1}", name, valueStr);
            logger.info(name+""+ valueStr);
            params.put(name, valueStr);
        }
		
		/*Map parameterMap = request.getParameterMap();
		if(null==parameterMap) {
			return ;
		}
		logger.info(parameterMap.toString());
		Set entrySet = parameterMap.entrySet();
		
		Iterator iterator = entrySet.iterator();
		
		while(iterator.hasNext()) {
			Object next = iterator.next();
			logger.info(next.toString());
		}*/
		
	}
	
}
