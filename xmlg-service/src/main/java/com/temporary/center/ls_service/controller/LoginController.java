package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.common.ValidateParam;
import com.temporary.center.ls_service.dao.UserDao;
import com.temporary.center.ls_service.domain.User;
import com.temporary.center.ls_service.domain.UserAddress;
import com.temporary.center.ls_service.service.LogUserService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

/**
 * 登录相关的接口
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/user")
public class LoginController {


    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	
	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private LogUserService logUserService;

	@Autowired
	private UserDao userService;

	@Autowired
	SMSUtil smsService;

	@Value("${fileBasePath}")
	String fileBasePath;
	@Value("${staticUrlPath}")
	String staticUrlPath;
	/**
	 * 发送验证码
	 * @return
	 */
	@RequestMapping(value = "/getVerifyCode.do", method = RequestMethod.POST)
    @ResponseBody
	public Json getVerifyCode(@RequestParam("phoneNum") String phoneNum){
		logger.info("getVerifyCode>>>>>>>>>>>>");
		Json json=new Json();
		//从哪里获取验证码
		if(StringUtil.isEmpty(phoneNum)) {
			json.setSattusCode(StatusCode.PARAMS_NO_NULL);
			return json;
		}
		String key=RedisKey.SMS+phoneNum;
		//未到再次发送时间，返回错误描述
        if(redisBean.exists(key)) {
        	 json.setSattusCode(StatusCode.SMS_ERROR);
			return json;
        }else {
        	//TODO 先暂时写死，极光可用时再开发
			String yzm ="1234";
            redisBean.setex(key, yzm, Constant.ONE_MINUTE);
        }
		json.setSuc("发送成功");
		return json;
	}
	
	//登陆接口
	@RequestMapping(value = "/getToken.do", method = RequestMethod.POST)
    @ResponseBody
    public Json getToken(String username,String password,String veriftyCode,String loginType,String third_id) {
		Json  json=new Json();
		json.setSuc("请求成功");
		long currentTimeMillis = System.currentTimeMillis();
		logger.info("start login>>>>>>>>>>>>");
		//校验密码或短验
        if("1".equals(loginType)){
			String md5Password = MD5Utils.getMD5(password);
			Map<String,Object> params = new HashedMap();
			params.put("phone",username);
			params.put("password",md5Password);
			List<User> list = userService.queryUserByParams(params);
			if(list==null){
				logger.info("账号或密码错误：",username+" "+password);
				json.setSattusCode(StatusCode.USERNAME_PASS_ERROR);
				return json;
			}
		}else if("2".equals(loginType)){
			int code = smsService.checkSMS(username,veriftyCode);
			if( code!= 200){
				switch (code){
					case 467:json.setSattusCode(StatusCode.SMS_TIMES_ERROR);break;
					case 468:json.setSattusCode(StatusCode.SMS_CODE_ERROR);break;
					default:json.setSattusCode(StatusCode.SMS_CHECK_ERROR);
				}

				return json;
			}

		}else {
			if(StringUtil.isEmpty(third_id)){
				json.setSattusCode(StatusCode.TYPE_NULL);
				json.setMsg("三方id为空");
				return json;
			}
			if("qq".equals(loginType)){
				Map<String,Object> params = new HashedMap();
				params.put("phone",username);
				params.put("qqKey",third_id);
				if(null == userService.queryUserByParams(params)){
					logger.info("三方ID错误：",username+" "+password);
					json.setSattusCode(StatusCode.ThirdID_ERROR);
					return json;
				}



			}else if("wechart".equals(loginType)){
				Map<String,Object> params = new HashedMap();
				params.put("phone",username);
				params.put("wxKey",third_id);
				if(null == userService.queryUserByParams(params)){
					logger.info("三方ID错误：",username+" "+password);
					json.setSattusCode(StatusCode.ThirdID_ERROR);
					return json;
				}

			}else if("weibo".equals(loginType)){
				Map<String,Object> params = new HashedMap();
				params.put("phone",username);
				params.put("wbKey",third_id);
				if(null == userService.queryUserByParams(params)){
					logger.info("三方ID错误：",username+" "+password);
					json.setSattusCode(StatusCode.ThirdID_ERROR);
					return json;
				}
			}else {
				json.setSattusCode(StatusCode.LOGIN_TYPE_ERROR);
				return json;
			}

		}


        Map<String,Object> params = new HashedMap();
        params.put("phone",username);
		User user = userService.queryUserByParams(params).get(0);
		//缓存token信息
		String token = UUID.randomUUID().toString();
		Map<String,String> token_params = new HashMap<>();
		token_params.put("user_id",user.getId()+"");
		token_params.put("user_name",user.getUserName());
		token_params.put("user_img",user.getUserImageUrl()==null?"":user.getUserImageUrl());
		//设置5天有效期
		redisBean.hset(RedisKey.USER_TOKEN+token,token_params,Constant.TOKEN_EFFECTIVE_TIME);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("token",token);
		result.put("timeStamp",System.currentTimeMillis());
		json.setData(result);
		long currentTimeMillis_end = System.currentTimeMillis();
		logger.info("login end>>>>>>>>>>>>");
		return json;
	}
	/**
	 * 注册用户
	 * @return
	 */
	@RequestMapping(value = "/createUser.do", method = RequestMethod.POST)
    @ResponseBody
	public Json createUser(String phone,String password,String verifyCode,String third_id,String thirdType) {
		logger.info("createUser"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			if(null==phone || phone.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(phone)");
				return json;
			}
			if(null==password || password.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(password)");
				return json;
			}
			
			if(null==verifyCode || verifyCode.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(verifyCode)");
				return json;
			}
			//判断验证码是否有用
			int code = smsService.checkSMS(phone,verifyCode);
			if( code!= 200){
				switch (code){
					case 467:json.setSattusCode(StatusCode.SMS_TIMES_ERROR);break;
					case 468:json.setSattusCode(StatusCode.SMS_CODE_ERROR);break;
					default:json.setSattusCode(StatusCode.SMS_CHECK_ERROR);
				}

				return json;
			}
			//判断是否有相同的电话号码
			User userParam=new User();
			userParam.setPhone(phone);
			userParam.setEffective(Constant.EFFECTIVE);
			Long count=logUserService.countDataByParams(userParam);
			if(null!=count && count>0) {
				json.setSattusCode(StatusCode.TEL_REGISTERED);
				return json;
			}
			User user = new User();
			user.setEmployeeReputation(0);//默认雇员信誉0
			user.setBossReputation(0);//默认雇主信誉
            user.setUserName(phone);
			user.setPassword(MD5Utils.getMD5(password));
			user.setEffective(Constant.EFFECTIVE);
			user.setCreateBy("system");
			user.setCreateTime(new Date());
			user.setPhone(phone);
			user.setUserImageUrl(staticUrlPath+"/user/header/user_header_default.jpeg");//头像地址

			//如果三方登录类别，三方登录id，都不为空，说明用户授权了三方登录。需要绑定关系，一起存下来
			if(StringUtil.isNotEmpty(third_id)&&StringUtil.isNotEmpty(thirdType)){
				switch (thirdType){
					case "qq":{
						user.setQqKey(third_id);break;
					}
					case "wechart":{
						user.setWxKey(third_id);break;
					}
					case "weibo":{
						user.setWbKey(third_id);break;
					}
				}
			}
			userService.insert(user);
			Map<String,Object> params = new HashedMap();
			params.put("phone",phone);
			User createdUser = userService.queryUserByParams(params).get(0);
			//生成IM账号
			//todo


			json.setSuc("注册成功");
			Map<String,String> jgResultMap=new HashMap<String,String>();
			jgResultMap.put("imAccount", phone);
			//TODO  生成用户存储空间
			createUserSpace(createdUser.getId());
			json.setData(jgResultMap);
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info("createUser"+Constant.METHOD_END);
		return json;
	}
	
	/**
	 * 查询我的个人信息
	 * @return
	 */
	@RequestMapping(value = "/myInfo.do", method = RequestMethod.POST)
    @ResponseBody
	public Json myInfo(String token,String sign,String timeStamp,String random) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		logger.info("myInfo"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			//判断参数
			if(null==token || token.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(token)");
				return json;
			}
			if(null==sign || sign.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(sign)");
				return json;
			}
			if(null==timeStamp || timeStamp.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(timeStamp)");
				return json;
			}
			if(null==random || random.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"(random)");
				return json;
			}
			
			if(!redisBean.exists(token)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			if(null==userId) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			Map<String,String> params=new HashMap<String,String>();
	        params.put("token", token);
	        params.put("timeStamp", timeStamp);
	        params.put("random", random);
	        String signGenerateByParams=createSign(params, false);
			
	        if(!signGenerateByParams.equals(sign)) {
	        	logger.debug("sign="+signGenerateByParams);
	        	json.setSattusCode(StatusCode.SIGN_ERROR);
				return json;
	        }
			
			//根据userId 查询用户信息
			User user = logUserService.getUserById(Long.parseLong(userId));
			user.setPassword(null);
			json.setData(user);
			json.setSuc();
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		logger.info("myInfo"+Constant.METHOD_END);
		return json;
	}
	
	
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping(value = "/updatePassword.do", method = RequestMethod.POST)
    @ResponseBody
    public Json updatePassword(String phoneNum,String verifyCode,String newPassword) {
		Json json=new Json();
		json.setSuc();
		json.setData("");
		try {
            int code = smsService.checkSMS(phoneNum,verifyCode);
            if( code!= 200){
                switch (code){
                    case 467:json.setSattusCode(StatusCode.SMS_TIMES_ERROR);break;
                    case 468:json.setSattusCode(StatusCode.SMS_CODE_ERROR);break;
                    default:json.setSattusCode(StatusCode.SMS_CHECK_ERROR);
                }

                return json;
            }
            Map<String,Object> queryUser = new HashedMap();
            queryUser.put("phone",phoneNum);
            List<User> userList = userService.queryUserByParams(queryUser);
            if(userList ==null){
                json.setSattusCode(StatusCode.USER_NULL_REGISTERED);
                return json;
            }

            Map<String,Object> update = new HashedMap();
            update.put("id",userList.get(0));
            update.put("password",MD5Utils.getMD5(newPassword));
            update.put("updateTime",new Date());
            logUserService.updatePassword(update);
            json.setSuc("密码重置成功");

		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		return json;
	}
	
	
	
	
	/**
	 * 修改用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateUser.do", method = RequestMethod.POST)
    @ResponseBody
    public Json updateUser(HttpServletRequest request,
            HttpServletResponse response) {
		Json json=new Json();
		json.setSuc();
		json.setData("");
		try {
			//判断token
			String token = request.getParameter("token");
			
			if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			String userId=redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			
	        String time = request.getParameter("timeStamp");
	        String sign=request.getParameter("sign");
	        String chineseName=request.getParameter("chineseName");
	        String sex=request.getParameter("sex");
	        String city=request.getParameter("city");
	        String birthday=request.getParameter("birthday");
	        String height=request.getParameter("height");
	        String qq=request.getParameter("qq");
	        String wx=request.getParameter("wx");
	        
	        Long timeStamp=Long.parseLong(time);
	        
	        Date currDate=new Date();
	        long countTime=currDate.getTime()-timeStamp;
	        if(countTime<0 || countTime>Constant.INTERFACE_TIME_OUT) {
	        	logger.info("时间相差countTime="+countTime);
	    		json.setSattusCode(StatusCode.TIME_OUT_FIVE_MINUTE);
	    		return json;
	        }
	        
	        
	        Map<String,String> params=new HashMap<String,String>();
	        params.put("time", time);
	        params.put("token", token);
	        params.put("chineseName", chineseName);
	        params.put("sex", sex);
	        params.put("city", city);
	        params.put("birthday", birthday);
	        params.put("height", height);
	        params.put("qq", qq);
	        params.put("wx", wx);
	        
	        String signGenerateByParams=createSign(params, false);
	        
	        
	        if(sign.equals(signGenerateByParams)) {
	        	Map<String,Object> param=new HashMap<String,Object>();
	        	param.put("id", userId);
	        	param.put("updateBy", userId);
	        	param.put("updateTime", new Date());
	        	param.put("chineseName", chineseName);
	        	param.put("sex", sex);
	        	param.put("city", city);
	        	param.put("birthday", birthday);
	        	param.put("height", height);
	        	param.put("qq", qq);
	        	param.put("wx", wx);
				logUserService.updateUser(param);
	        }else {
	        	logger.debug("签名不对，需要签名="+signGenerateByParams);
	        	json.setSattusCode(StatusCode.SIG_ERROR);
	        }
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		return json;
	}
	
	
	
	

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request,
            HttpServletResponse response) {
		Map<String, Object> result_map = new HashMap<String, Object>();
        result_map.put("success", 1);
        result_map.put("msg", "登录成功");
		String uuid=UUID.randomUUID().toString();
		logger.info("登录开始>>>>>>>>>>>>"+uuid);
		
		String id = request.getSession().getId();
		logger.info("sessionId", id);
		
		//String sign = request.getParameter("sign");
		Enumeration<?> pNames =  request.getParameterNames();
		Map<String, Object> params = new HashMap<String, Object>();
		while (pNames.hasMoreElements()) {
            String pName = (String) pNames.nextElement();
            if("sign".equals(pName))continue;
            Object pValue = request.getParameter(pName);
            params.put(pName, pValue);
        }
		
		/*String inputName = request.getParameter("username");
        String inputPassWord = request.getParameter("password");
        String inputCheckCode = request.getParameter("checkcode");*/
		
		
		logger.info("登录结束>>>>>>>>>>>>"+uuid);
		return result_map;
	}
	
	/**
	 * 生成签名
	 * @param params
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String createSign(Map<String, String> params, boolean encode)
            throws UnsupportedEncodingException {
		if(null==params || params.size()==0) {
			logger.info("params 为空");
			return null;
		}
		Set<Entry<String, String>> entrySet = params.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while(iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			logger.debug("key="+next.getKey()+",value="+next.getValue());
		}
		
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = String.valueOf(value);
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueString, "UTF-8"));
            } else {
                temp.append(valueString);
            }
        }
        return MD5Utils.getMD5(temp.toString()).toUpperCase();
    }
	
	/**
	 *地址列表
	 * @return
	 */
	@RequestMapping(value = "/addressList.do", method = RequestMethod.POST)
    @ResponseBody
	public Json addressList(HttpServletRequest request,
                            HttpServletResponse response, String token, String sign, String timeStamp, String random) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="地址列表,"+uuid;
		logger.info(title+",list"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			
			//判断token
			if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			String userId=redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			
			UserAddress userAddress=new UserAddress();
			userAddress.setCreateby(Long.parseLong(userId));
			userAddress.setPageSize(20);
			userAddress.setCurr(1);
			userAddress.setActive(Integer.parseInt(Constant.EFFECTIVE));
			List<UserAddress> list= logUserService.addressList(userAddress);
			for(UserAddress ua:list) {
				ua.setCity("");
			}
			if(null==list || list.size()==0) {
				json.setData(StatusCode.NO_DATA.getMessage());
				json.setSattusCode(StatusCode.NO_DATA);
			}else {
				json.setData(list);
				json.setSuc();
			}
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",list"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	/**
	 * 新增地址
	 * @return
	 */
	@RequestMapping(value = "/addAddress.do", method = RequestMethod.POST)
    @ResponseBody
	public Json addAddress(UserAddress userAddress,@RequestParam("token") String token,String sign,String timeStamp) {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="新增地址,"+uuid;
		logger.info(title+",createUser"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		try {
			//判断token
			if(!redisBean.exists(RedisKey.USER_TOKEN+token)) {
				json.setSattusCode(StatusCode.TOKEN_ERROR);
				return json;
			}
			
			String userId=redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
			
			//判断参数
			json=ValidateParam.validateParamNotNull(userAddress);
 			if(!json.getCode().equals(StatusCode.SUC.getCode())) {
 				return json;
 			}
			 
			userAddress.setCreateby(Long.parseLong(userId));
			userAddress.setCreatetime(new Date());
			userAddress.setActive(Integer.parseInt(Constant.EFFECTIVE) );
			
			Integer isdefault = userAddress.getIsdefault();
			if(null!=isdefault && isdefault.equals(0)) {//是否默认 0：默认 1：非默认
				UserAddress updateUserAddress=new UserAddress();
				updateUserAddress.setCreateby(Long.parseLong(userId));
				updateUserAddress.setIsdefault(1);
				logUserService.updateIsdefault(updateUserAddress);
			}
			
			logUserService.addAddress(userAddress);
			
			
			
			
			json.setData("新增成功");
			json.setSuc();
		}catch(Exception e) {
			e.printStackTrace();
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",createUser"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	public void createUserSpace(int id){
		String[] ss = new String[]{fileBasePath+"/user/"+id+"/header",fileBasePath+"/user/"+id+"/icard",fileBasePath+"/user/"+id+"/business"};
		for(String s:ss){
			File f = new File(s);
			if(!f.exists()){
				f.mkdirs();
			}
		}
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		Map<String,String> params=new HashMap<String,String>();
        params.put("password", "2");
        params.put("time", "20190323 21:48:10");
        params.put("token", "8593286d-03e8-44fa-9286-a29244d1f73a");
        
        String signGenerateByParams=createSign(params, false);
        System.out.println(signGenerateByParams);
        
	}
	
}
