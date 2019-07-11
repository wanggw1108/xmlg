package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.LogAnno;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.domain.Logtable;
import com.temporary.center.ls_service.service.comm.LogtableService;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class LogAopAspect {
	
	private static final LogUtil logger = LogUtil.getLogUtil(LogAopAspect.class);

	
	@Autowired
    private  LogtableService logtableService;// 日志Service
	
	@Autowired
	private RedisBean redisBean;
	
	 @Around("@annotation(com.temporary.center.ls_service.common.LogAnno)")
	 public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		 Json jsonBefor=null;
		//1.这里获取到所有的参数值的数组
	    Object[] args = pjp.getArgs();
	    
		// 1.方法执行前的处理，相当于前置通知
		// 获取方法签名
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		 // 获取方法
        Method method = methodSignature.getMethod();
        
        String[] parameterNames = methodSignature.getParameterNames();
        
        
        int tokenIndex = ArrayUtils.indexOf(parameterNames, "token");
        if (tokenIndex != -1) {//参数中有token 则进行下面的检查
        	String token=(String)args[tokenIndex];
        	if(null!=token && redisBean.exists(token)) {//检查token 是否过期
        		int timeStampIndex = ArrayUtils.indexOf(parameterNames, "timeStamp");
                if (timeStampIndex != -1) {//检查参数中是否含有时间戳
                	String timeStampstr = (String) args[timeStampIndex];
                	long timeStamp=Long.parseLong(timeStampstr);
                	if (System.currentTimeMillis() - timeStamp >Constant.INTERFACE_TIME_OUT) {//超过时间
                		jsonBefor=new Json();
                		jsonBefor.setSattusCode(StatusCode.TIME_OUT_FIVE_MINUTE);
                	}else {//没有超过时间 检查签名 sign
                		int signIndex = ArrayUtils.indexOf(parameterNames, "sign");
                		if(signIndex!=-1) {//有签名，验证签名是否合法
                			Map<String,String>  allParamsMap=new HashMap<String,String>();
                			allParamsMap=getAllParamsMap(parameterNames,args);
                			String createSign = LoginController.createSign(allParamsMap, false);
                			String sign=(String)args[signIndex];
                			if(!sign.equals(createSign)) {//签名错误
                				logger.debug("正确的签名="+createSign);
                				jsonBefor=new Json();
                    			jsonBefor.setSattusCode(StatusCode.SIGN_ERROR);
                			}
                			 
                		}else {//没有签名sign
                			jsonBefor=new Json();
                			jsonBefor.setSattusCode(StatusCode.NO_SIGN);
                		}
                	}
                }else {//没有时间戳
                	jsonBefor=new Json();
                	jsonBefor.setSattusCode(StatusCode.NO_TIME_STAMP);
                }
                
        	}else {//token过期
        		jsonBefor=new Json();
        		jsonBefor.setSattusCode(StatusCode.TOKEN_ERROR);
        	}
        }
        
        // 获取方法上面的注解
        LogAnno logAnno = method.getAnnotation(LogAnno.class);
        // 获取操作描述的属性值
        String operateType = logAnno.operateType();
        // 创建一个日志对象(准备记录日志)
        Logtable logtable = new Logtable();
        logtable.setOperatetype(operateType);// 操作说明
        logtable.setMethod(method.getName());
        Object result = null;
        try {
	        //让代理方法执行
        	if(null!=jsonBefor) {
        		result=jsonBefor;
        	}else {
        		result = pjp.proceed();
        	}
	        // 2.相当于后置通知(方法成功执行之后走这里)
	        logtable.setOperateresult("正常");// 设置操作结果
        }catch(Exception e) {
        	// 3.相当于异常通知部分
            logtable.setOperateresult("失败");// 设置操作结果
        }finally {
            // 4.相当于最终通知
            logtable.setOperatedate(new Date());// 设置操作日期
            logtableService.addLog(logtable);
        }
        return result;
	 }

	private Map<String, String> getAllParamsMap(String[] parameterNames, Object[] args) {
		Map<String, String> result=new HashMap<String,String>();
		for(int i=0;null!=args && null!=parameterNames && i<parameterNames.length;i++) {
			if("sign".equals(parameterNames[i])) {
				continue;
			}
			result.put(parameterNames[i], (String)args[i]);
		}
		return result;
	}
}
