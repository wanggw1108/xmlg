package com.temporary.center.ls_service.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
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

import java.util.Date;

/**
 * @author wangguowei
 * @description
 * @create 2019-08-05-11:48
 */
@Controller
@RequestMapping(value = "/payback")
public class PayBackController {

    private static final Logger logger = LoggerFactory.getLogger(PayBackController.class);
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

    /**
     * 退款
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping(value = "/back.do", method = RequestMethod.GET)
    @ResponseBody
    public Json refund(String token, String orderNo){

        Json json = new Json();
        if(!redisService.exists(RedisKey.USER_TOKEN+token)){
            json.setSattusCode(StatusCode.TOKEN_ERROR);
            return json;
        }
        Order order = new Order();
        order.setOrderNo(orderNo);
        order = orderService.selectOne(order);
        if("alipay".equals(order.getPayType())){
            return alipayRefund(orderNo,order);
        }else{
            json.setSattusCode(StatusCode.DATA_ERROR);
            return json;
        }

    }

    public Json alipayRefund(String orderNo,Order order) {

        Json json = new Json();

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



                json.setSuc("退款成功");
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






}
