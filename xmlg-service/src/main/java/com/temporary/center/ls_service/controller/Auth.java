package com.temporary.center.ls_service.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/payauth")
public class Auth {

	private static final LogUtil logger = LogUtil.getLogUtil(Auth.class);

	@RequestMapping(value = "/start.do")
	public void start(HttpServletRequest request,
            HttpServletResponse response) {
		logger.info("start"+Constant.METHOD_BEGIN);
		/*Map parameterMap = request.getParameterMap();
		Set entrySet = parameterMap.entrySet();
		Iterator iterator = entrySet.iterator();
		while(iterator.hasNext()) {
			Object next = iterator.next();
			
		}*/
		

		String APP_ID="2016092500595098";
		String APP_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDHkha+fT3BuvI10LrwRtFvmsnpNF0TTy183uq5pbzXpotNb9Ad7w+LAKdy1rrocfUQ99YfY2gtkGY26Jfli+ketx7aBlwzXLKRkmbQGvgYvkAbUZhLCNKSPU2vWD3APDSD5Zeeu5lV79/V4ArbUz/Fa9mhUTH3lNHssZITx7gBKTsoWeHzFZsWzoFIhIAcPvFHtQAIERlkV51tAeHGdGV9AJecThNNn1+1MhXiZNKV1MGNwxV4/ha22AGp2fHJI3ynw2Jcg2t2YzL96gCbutujWCk3sDlNB8RVMUHqOuKZqFPKPxR7QcpsU8GHYXqujvJjW6/f9tmGIF+9ZPQPoAcVAgMBAAECggEASc44RRhyN2xY2RgpmmTGXbzkAS/RYCu/Cdgy/JdTwHkiOXApinh3yhNi2fCZzQ8f77eIY7FSvM8TTik0y7qzax2T9lO103TrwTRge4QNzrDFapUBenZ30YeEbF148NgTH9hP+28rpQGCSUmDGluuUOey45rwnLxX6m49NL1+ykZ6SiCPXo6BJrh2RSrgtWu1OvNEBsFpbZfG7Yoo80M5R5vMpk6FCeyeTm5MQ3Y4V2AxYozZC4l1YTrF9mWzV5q5PGKrnna9A4A0Y/OOUwJwrea9KXBxz3dgpZopGgul9UjcJmXPLy2JeUoJbS7sPLf06pHHnNPBtq4V6WyPY77FgQKBgQD5yIA/MA3H/mj12Sxe4NpTFiFY+vY64r+FzwDRBKmbK53fm0gzO/suKokVjXWMKZwxiPcc/QSjLtWhWZWAuJrP6kQypzluNMcqptANj0ciOLTbmQ982JXKXRdc2dEq7QG4AY5OMQWgZGvQy8ijy5MimELPL6XT+u1XLPl6EEseYQKBgQDMiahWA7SLHvh06MnrYcWzR518gXn4gr5FCua1Ch8SfggqNrWr7atqbhUkWhrpCNrrT9xrpvfuYqtyKYHNGg95LHHjZ3ud5ZEXU5sU+sM4THI9iAHoQgdxeNDu39bbvCs5l+9h0ieKIQyzTL3IOCVbeZycDE+O7zIDYhlQW3ndNQKBgB3Q6DYit1JrNP47yUceEhGe76Yeduv6iyAe4JsSsmg6YaMCdzDedRJCs5BmDWUIxoNHNTA5p6cO/SNAfU8bOiedcDyXp4xXMnsN5IHp23TjTft0ntupQuAgEpzbx1MXbYAYd3Mv+Nr3gcMvwCiFMXAMutTefC38ov4n+TOv5DehAoGAHg7QxSswOg2oF44qNkQdCJaqpyRj50hXa9AJTWuaW15YFtQJaKW5vh9FTh1LvRlHCrOmwc88ujSX22QMJjcBNR558iBAA1k8NEBeFvwu+jQJyc8V8KrbayqFEcWlTo9djs4sKKT4mIz4z9SkQys1GjdJ84i/nFK8d9DPxYZb500CgYEAuyuybhcXWtC5hrC8PsuadklJYovzs7HRG+fAzcuP9RrzRcdrMPrHDNA40qnLFrHpaVw+/zAudW6laU7Lmh+YAnXPIq+RmGVpR2btUb+CEwHlarFSTWSQnjAhpO+jwMYUVIziYqtI5+5YYk7lrZKmVAasOI7JOkHl1hflT7+Cb3I=" + 
				"";
		String CHARSET="UTF-8";
		String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx5IWvn09wbryNdC68EbRb5rJ6TRdE08tfN7quaW816aLTW/QHe8PiwCncta66HH1EPfWH2NoLZBmNuiX5YvpHrce2gZcM1yykZJm0Br4GL5AG1GYSwjSkj1Nr1g9wDw0g+WXnruZVe/f1eAK21M/xWvZoVEx95TR7LGSE8e4ASk7KFnh8xWbFs6BSISAHD7xR7UACBEZZFedbQHhxnRlfQCXnE4TTZ9ftTIV4mTSldTBjcMVeP4WttgBqdnxySN8p8NiXINrdmMy/eoAm7rbo1gpN7A5TQfEVTFB6jrimahTyj8Ue0HKbFPBh2F6ro7yY1uv3/bZhiBfvWT0D6AHFQIDAQAB" + 
				"";
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", 
				APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest requestAli = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("我是测试数据");
		model.setSubject("App支付测试Java");
		model.setOutTradeNo("324234");
		model.setTimeoutExpress("30m");
		model.setTotalAmount("0.01");
		model.setProductCode("QUICK_WAP_WAY");
		model.setSellerId("2088102177244002");
		requestAli.setBizModel(model);
		requestAli.setNotifyUrl("http://47.107.103.97:8081/payauth/authRedirect.do");
		try {
		        //这里和普通的接口调用不同，使用的是sdkExecute
		        //AlipayTradeAppPayResponse response2 = alipayClient.sdkExecute(requestAli);
		        //System.out.println(response2.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
		        
		        String form = alipayClient.pageExecute(requestAli).getBody();
		        
		        logger.info("form ={0}", form);
		        
		        
		        response.setContentType("text/html;charset=UTF-8;"); 
			    try {
					response.getWriter().write(form);
					//直接将完整的表单html输出到页面 
				    response.getWriter().flush(); 
				    response.getWriter().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		}
	
		
		
	}
	
	
	public static void main(String[] args) {
		String APP_ID="2016092500595098";
		String APP_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDHkha+fT3BuvI10LrwRtFvmsnpNF0TTy183uq5pbzXpotNb9Ad7w+LAKdy1rrocfUQ99YfY2gtkGY26Jfli+ketx7aBlwzXLKRkmbQGvgYvkAbUZhLCNKSPU2vWD3APDSD5Zeeu5lV79/V4ArbUz/Fa9mhUTH3lNHssZITx7gBKTsoWeHzFZsWzoFIhIAcPvFHtQAIERlkV51tAeHGdGV9AJecThNNn1+1MhXiZNKV1MGNwxV4/ha22AGp2fHJI3ynw2Jcg2t2YzL96gCbutujWCk3sDlNB8RVMUHqOuKZqFPKPxR7QcpsU8GHYXqujvJjW6/f9tmGIF+9ZPQPoAcVAgMBAAECggEASc44RRhyN2xY2RgpmmTGXbzkAS/RYCu/Cdgy/JdTwHkiOXApinh3yhNi2fCZzQ8f77eIY7FSvM8TTik0y7qzax2T9lO103TrwTRge4QNzrDFapUBenZ30YeEbF148NgTH9hP+28rpQGCSUmDGluuUOey45rwnLxX6m49NL1+ykZ6SiCPXo6BJrh2RSrgtWu1OvNEBsFpbZfG7Yoo80M5R5vMpk6FCeyeTm5MQ3Y4V2AxYozZC4l1YTrF9mWzV5q5PGKrnna9A4A0Y/OOUwJwrea9KXBxz3dgpZopGgul9UjcJmXPLy2JeUoJbS7sPLf06pHHnNPBtq4V6WyPY77FgQKBgQD5yIA/MA3H/mj12Sxe4NpTFiFY+vY64r+FzwDRBKmbK53fm0gzO/suKokVjXWMKZwxiPcc/QSjLtWhWZWAuJrP6kQypzluNMcqptANj0ciOLTbmQ982JXKXRdc2dEq7QG4AY5OMQWgZGvQy8ijy5MimELPL6XT+u1XLPl6EEseYQKBgQDMiahWA7SLHvh06MnrYcWzR518gXn4gr5FCua1Ch8SfggqNrWr7atqbhUkWhrpCNrrT9xrpvfuYqtyKYHNGg95LHHjZ3ud5ZEXU5sU+sM4THI9iAHoQgdxeNDu39bbvCs5l+9h0ieKIQyzTL3IOCVbeZycDE+O7zIDYhlQW3ndNQKBgB3Q6DYit1JrNP47yUceEhGe76Yeduv6iyAe4JsSsmg6YaMCdzDedRJCs5BmDWUIxoNHNTA5p6cO/SNAfU8bOiedcDyXp4xXMnsN5IHp23TjTft0ntupQuAgEpzbx1MXbYAYd3Mv+Nr3gcMvwCiFMXAMutTefC38ov4n+TOv5DehAoGAHg7QxSswOg2oF44qNkQdCJaqpyRj50hXa9AJTWuaW15YFtQJaKW5vh9FTh1LvRlHCrOmwc88ujSX22QMJjcBNR558iBAA1k8NEBeFvwu+jQJyc8V8KrbayqFEcWlTo9djs4sKKT4mIz4z9SkQys1GjdJ84i/nFK8d9DPxYZb500CgYEAuyuybhcXWtC5hrC8PsuadklJYovzs7HRG+fAzcuP9RrzRcdrMPrHDNA40qnLFrHpaVw+/zAudW6laU7Lmh+YAnXPIq+RmGVpR2btUb+CEwHlarFSTWSQnjAhpO+jwMYUVIziYqtI5+5YYk7lrZKmVAasOI7JOkHl1hflT7+Cb3I=" + 
				"";
		String CHARSET="UTF-8";
		String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx5IWvn09wbryNdC68EbRb5rJ6TRdE08tfN7quaW816aLTW/QHe8PiwCncta66HH1EPfWH2NoLZBmNuiX5YvpHrce2gZcM1yykZJm0Br4GL5AG1GYSwjSkj1Nr1g9wDw0g+WXnruZVe/f1eAK21M/xWvZoVEx95TR7LGSE8e4ASk7KFnh8xWbFs6BSISAHD7xR7UACBEZZFedbQHhxnRlfQCXnE4TTZ9ftTIV4mTSldTBjcMVeP4WttgBqdnxySN8p8NiXINrdmMy/eoAm7rbo1gpN7A5TQfEVTFB6jrimahTyj8Ue0HKbFPBh2F6ro7yY1uv3/bZhiBfvWT0D6AHFQIDAQAB" + 
				"";
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", 
				APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest requestAli = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("我是测试数据");
		model.setSubject("App支付测试Java");
		model.setOutTradeNo("324234");
		model.setTimeoutExpress("30m");
		model.setTotalAmount("0.01");
		model.setProductCode("QUICK_MSECURITY_PAY");
		model.setSellerId("2088102177244002");
		requestAli.setBizModel(model);
		
		requestAli.setNotifyUrl("http://47.107.103.97:8081/payauth/authRedirect.do");
		try {
		        //这里和普通的接口调用不同，使用的是sdkExecute
		        //AlipayTradeAppPayResponse response2 = alipayClient.sdkExecute(requestAli);
		        //S//ystem.out.println(response2.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
		        
		        //String form = alipayClient.pageExecute(requestAli).getBody();
		        
		        //logger.info("form ={0}", form);
			AlipayTradeAppPayResponse response = alipayClient.pageExecute(requestAli);
			if(response.isSuccess()) {
				logger.info("调用成功{0}", response.getBody());
			}
		        
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/authRedirect.do")
    @ResponseBody
	public void authRedirect(HttpServletRequest request,
            HttpServletResponse response) {
		logger.info("authRedirect"+Constant.METHOD_BEGIN);
	}
	
}
