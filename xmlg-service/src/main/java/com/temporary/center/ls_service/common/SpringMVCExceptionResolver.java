package com.temporary.center.ls_service.common;


import com.temporary.center.ls_common.LogUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class SpringMVCExceptionResolver implements HandlerExceptionResolver {

	private static final LogUtil logger = LogUtil.getLogUtil(SpringMVCExceptionResolver.class);

	public ModelAndView resolveException(HttpServletRequest arg0,
                                         HttpServletResponse arg1, Object arg2, Exception e) {
		logger.error(e, "发现未捕获异常");

		Map<String,Object> map = new HashMap<String,Object>();
        map.put("errorMsg", e.getMessage());
        return new ModelAndView("error", map);
	}

}
