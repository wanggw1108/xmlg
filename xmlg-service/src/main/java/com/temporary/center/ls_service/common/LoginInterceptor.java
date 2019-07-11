package com.temporary.center.ls_service.common;


import com.temporary.center.ls_common.LogUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <判断用户登陆>
 * <功能详细描述>
 * 
 * @author  huangjin
 * @version  [版本号, 2015年3月9日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    
    private static final List<String> IGNORE_URI = new ArrayList<String>();
    
    private static final List<String> PERMISSION_URI = new ArrayList<String>();
    
    static{
        IGNORE_URI.add("/login.html");
        IGNORE_URI.add("/user/");
    }
    
    private static final LogUtil logger = LogUtil.getLogUtil(LoginInterceptor.class);
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String url = request.getRequestURL().toString();
    	logger.info("拦截请求"+url);
        boolean flag = true;
        return flag;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
    
}
