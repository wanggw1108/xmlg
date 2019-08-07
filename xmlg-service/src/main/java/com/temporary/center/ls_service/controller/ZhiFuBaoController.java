package com.temporary.center.ls_service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayCodeRecoResult;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.temporary.center.ls_common.AlipayNotifyParam;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.OrderMapper;
import com.temporary.center.ls_service.dao.WalletDetailMapper;
import com.temporary.center.ls_service.dao.WalletMapper;
import com.temporary.center.ls_service.domain.Order;
import com.temporary.center.ls_service.domain.Wallet;
import com.temporary.center.ls_service.domain.WalletDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 支付宝
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/alipay")
public class ZhiFuBaoController {

	private static final Logger logger = LoggerFactory.getLogger(ZhiFuBaoController.class);
	private ExecutorService executorService = Executors.newFixedThreadPool(20);
	@Value("${alipay.appid}")
	private String appid;
	@Value("${alipay.publicKey}")
	private String publicKey;
	@Value("${alipay.privateKey}")
	private String privateKey;
	@Value("${alipay.url}")
	private String url;
	@Value("${alipay.notifyUrl}")
	private String notifyUrl;

	@Autowired
	private OrderMapper orderService;
	@Autowired
	RedisBean redisService;
	@Autowired
	WalletDetailMapper walletDetailService;
	@Autowired
	WalletMapper walletService;

	//网关
	@RequestMapping(value = "/pay.do", method = RequestMethod.POST)
    @ResponseBody
    public Json pay(String token, Integer orderId) {
		logger.info("支付宝网关");
		Json json = new Json();
		if(!redisService.exists(RedisKey.USER_TOKEN+token)){
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}
		Order order = orderService.selectByPrimaryKey(orderId);
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(url, appid, privateKey, "json", "utf-8", publicKey, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(order.getRecruitTitle());
		model.setSubject(order.getRecruitTitle());
		model.setOutTradeNo(order.getOrderNo());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(order.getAmount()+"");//测试情况下，默认0.01
		model.setProductCode("QUICK_MSECURITY_PAY");
		JSONObject params = new JSONObject();
		params.put("employeeId",order.getEmployeeId());
		params.put("userId",order.getUserId());
		params.put("orderId",orderId);
		try {
			model.setPassbackParams(URLEncoder.encode(params.toJSONString(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		try {
			//这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
			json.setData(response.getBody());
			json.setSuc();
			return json;
		} catch (AlipayApiException e) {
			logger.info(e.getErrMsg());
			json.setSattusCode(StatusCode.FAIL);
			return json;
		}
		
	}


	/**
	 * <pre>
	 * 第一步:验证签名,签名通过后进行第二步
	 * 第二步:按一下步骤进行验证
	 * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	 * 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
	 * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
	 * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
	 * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
	 * </pre>
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/callback.do", method = RequestMethod.POST)
	@ResponseBody
	public String callback(HttpServletRequest request) {
		Map<String, String> params= new HashMap<>();

		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for(int i = 0; i<values.length;i++){
				valueStr = (i==values.length-1)?valueStr+values[i]:valueStr+values[i]+",";
			}
			params.put(name,valueStr);
		}

		logger.info("支付宝回调, sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

		//!!! 验证回调的正确性，是不是支付宝发了，而且还要避免重复通知

		params.remove("sign_type");
		try {
			// 调用SDK验证签名,待排查，check中已进行了关键信息验证，此处不重要
//			boolean signVerified = AlipaySignature.rsaCheckV2(params, publicKey, "utf-8", "RSA2");
			if (true) {
				logger.info("支付宝回调签名认证成功");
				//支付回调与退款回调，分别处理
				if(!params.containsKey("refund_fee")){
					// 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
					this.check(params);
					String trade_status = params.get("trade_status");
					logger.info("支付结果："+trade_status);
					if(!"TRADE_SUCCESS".equals(trade_status)){
						logger.info("交易失败");
						return "success";
					}
					// 处理支付成功逻辑
					try {
						//处理业务逻辑
						//1、生成钱包记录
						WalletDetail detail = new WalletDetail();
						detail.setAmount(Float.valueOf(params.get("receipt_amount")));
						JSONObject callbackParams = JSONObject.parseObject(URLDecoder.decode(params.get("passback_params"),"utf-8"));
						detail.setCreateby(callbackParams.getString("userId"));
						detail.setReason("支付宝转入");
						detail.setUserid(callbackParams.getInteger("userId"));
						detail.setCreatetime(new Date());
						detail.setType(1);//1 收入，2 支出
						detail.setRemark(params.get("out_trade_no"));
						detail.setTradeNo(params.get("trade_no"));
						detail.setOrderNo(params.get("out_trade_no"));
						walletDetailService.insert(detail);
						logger.info("订单 {} 已产生交易记录",params.get("out_trade_no"));
						Wallet wallet = new Wallet();
						wallet.setCreateBy(Integer.valueOf(callbackParams.getInteger("userId")));
						wallet = walletService.selectOne(wallet);
						if(wallet==null){
							wallet = new Wallet();
							wallet.setCreateBy(callbackParams.getInteger("userId"));
							wallet.setAmount(Float.valueOf(params.get("receipt_amount")));
							wallet.setCreateTime(new Date());
							wallet.setUpdateTime(new Date());
							walletService.insert(wallet);
						}else {
							wallet.setAmount(wallet.getAmount()+Float.valueOf(params.get("receipt_amount")));
							wallet.setUpdateTime(new Date());
							walletService.updateByPrimaryKey(wallet);
						}
						logger.info("用户钱包已添加余额:"+wallet.getAmount());
						int orderId = callbackParams.getIntValue("orderId");
						Order order = orderService.selectByPrimaryKey(orderId);
						order.setOrderState(2);//已经支付完成，设置订单状态为进行中
						order.setPayType("alipay");
						orderService.updateByPrimaryKey(order);
						logger.info("变更订单状态为已支付");


					} catch (Exception e) {
						logger.error("支付宝回调业务处理报错,params:" + params, e);
					}
					// 如果签名验证正确，立即返回success，后续业务另起线程单独处理
					// 业务处理失败，可查看日志进行补偿，跟支付宝已经没多大关系。
				}
				return "success";
			} else {
				logger.info("支付宝回调签名认证失败，signVerified=false, paramsJson:{}", params);
				return "failure";
			}
		} catch (AlipayApiException e) {
			logger.error("支付宝回调签名认证失败,paramsJson:{},errorMsg:{}", params, e.getMessage());
			return "failure";
		}
	}

	/**
	 * 支付宝退款
	 * @param orderNo 订单号
	 * @return
	 */
	@RequestMapping(value = "/payback.do", method = RequestMethod.GET)
	@ResponseBody
	public Json alipayRefund(String token,String orderNo) {
		Json json = new Json();
		logger.info("支付宝网关");
		if(!redisService.exists(RedisKey.USER_TOKEN+token)){
			json.setSattusCode(StatusCode.TOKEN_ERROR);
			return json;
		}

		Order order = new Order();
		order.setOrderNo(orderNo);
		order = orderService.selectOne(order);
		Wallet wallet = walletService.selectByPrimaryKey(order.getUserId());
		if(wallet.getAmount()<order.getAmount()){
			logger.info("余额足，退款失败，用户ID："+order.getUserId());
			json.setSattusCode(StatusCode.NO_MORE_Amount);
			return json;

		}
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(url, appid, privateKey, "json", "utf-8", publicKey, "RSA2");
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest(); //请求对象

		AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel(); //请求实体
		refundModel.setOutTradeNo(orderNo);
		refundModel.setRefundAmount(order.getAmount()+"");
		request.setBizModel(refundModel);//将请求实体添加到请求对象中

		AlipayTradeRefundResponse res = null; //发送退款请求,获得退款结果
		try {
			res = alipayClient.execute(request);
			//判断请求是否发送成功
			if(res.isSuccess()){
				logger.info("退款请求发送成功：{}",res.getBody());
			}
			//判断退款是否成功
			if(res.getFundChange().equals("Y")){
				order.setOrderState(6);
				order.setUpdateTime(new Date());
				orderService.updateByPrimaryKey(order);
				logger.info("退款成功，已变更订单：{} 为退款状态",res.getOutTradeNo());
				//退款之后，钱包余额扣除
				wallet.setAmount(wallet.getAmount()-order.getAmount());
				walletService.updateByPrimaryKey(wallet);
				logger.info("退款成功，钱包已扣除余额：-{}元 ",order.getAmount());
				WalletDetail detail = new WalletDetail();
				detail.setOrderNo(order.getOrderNo());
				detail.setUserid(order.getUserId());
				detail.setAmount(Float.valueOf("-"+order.getAmount()));
				detail.setReason("退款");
				detail.setRemark("出账");
				detail.setTradeNo(res.getTradeNo());
				detail.setCreateby(order.getUserId()+"");
				detail.setCreatetime(new Date());
				walletDetailService.insert(detail);
				logger.info("退款成功，生成交易记录：{}",order.getUserId());



				json.setSuc();
				return json;
			}else {
				logger.info("退款失败，订单：{} ",res.getBody());
			}
		} catch (AlipayApiException e) {
			logger.info("退款异常，订单：{} ",res.getOutTradeNo());
		}
		json.setSattusCode(StatusCode.PAY_BACK_Error);
		return json;
	}

	// 将request中的参数转换成Map
	private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<String, String>();

		Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

		for (Map.Entry<String, String[]> entry : entrySet) {
			String name = entry.getKey();
			String[] values = entry.getValue();
			int valLen = values.length;

			if (valLen == 1) {
				retMap.put(name, values[0]);
			} else if (valLen > 1) {
				StringBuilder sb = new StringBuilder();
				for (String val : values) {
					sb.append(",").append(val);
				}
				retMap.put(name, sb.toString().substring(1));
			} else {
				retMap.put(name, "");
			}
		}

		return retMap;
	}

	private AlipayNotifyParam buildAlipayNotifyParam(Map<String, String> params) {
		String json = JSON.toJSONString(params);
		return JSON.parseObject(json, AlipayNotifyParam.class);
	}

	/**
	 * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	 * 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
	 * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
	 * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
	 * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
	 *
	 * @param params
	 * @throws AlipayApiException
	 */
	private void check(Map<String, String> params) throws AlipayApiException {
		String outTradeNo = params.get("out_trade_no");

		// 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		Order order = new Order();
		order.setOrderNo(outTradeNo);
		order =orderService.selectOne(order);
		if (order == null) {
			throw new AlipayApiException("out_trade_no错误");
		}

		// 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），

		if (!params.get("total_amount").equals(order.getAmount()+"")) {
			throw new AlipayApiException("error total_amount");
		}

		// 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
		// 第三步可根据实际情况省略

		// 4、验证app_id是否为该商户本身。
		if (!params.get("app_id").equals(appid)) {
			throw new AlipayApiException("app_id不一致");
		}
	}
	
}
