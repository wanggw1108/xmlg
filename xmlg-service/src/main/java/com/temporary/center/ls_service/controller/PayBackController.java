package com.temporary.center.ls_service.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.temporary.center.ls_common.DateUtil;
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

    /**
     * 转账(提现)
     * @param
     * @return
     */
    @RequestMapping(value = "/transfer.do", method = RequestMethod.GET)
    @ResponseBody
    public Json transfer(String token, Float amount,String account,String realName){

        Json json = new Json();
        if(!redisService.exists(RedisKey.USER_TOKEN+token)){
            json.setSattusCode(StatusCode.TOKEN_ERROR);
            return json;
        }
        int user_id = Integer.valueOf(redisService.hget(RedisKey.USER_TOKEN+token,"user_id"));
        //转账操作，先扣除余额，再进行转账
        Wallet wallet = new Wallet();
        wallet.setCreateBy(user_id);
        wallet = walletService.selectOne(wallet);
        if(wallet.getAmount()<amount){
            json.setSattusCode(StatusCode.NO_MORE_Amount);
            json.setMsg("余额不足，请确认提现金额");
            return json;
        }
        //先扣除余额
        wallet.setAmount(wallet.getAmount() - amount);
        wallet.setUpdateTime(new Date());
        walletService.updateByPrimaryKey(wallet);
        logger.info("已扣除 {} 余额 {}",account,amount);
        AlipayClient alipayClient = new DefaultAlipayClient(url,appid,privateKey,"json","utf-8",publicKey,"RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        JSONObject transfer = new JSONObject();
        String out_biz_no = "OUT"+DateUtil.getyyyyMMddHHmmss()+(1000000000+user_id);
        transfer.put("out_biz_no",out_biz_no);
        transfer.put("payee_type","ALIPAY_LOGONID");
        transfer.put("payee_account",account);
        transfer.put("amount",amount);
        transfer.put("payee_real_name",realName);
        transfer.put("payer_show_name","用户提现");
        transfer.put("remark","用户提现");
        request.setBizContent(transfer.toJSONString());
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            wallet.setAmount(wallet.getAmount() + amount);
            wallet.setUpdateTime(new Date());
            walletService.updateByPrimaryKey(wallet);
            logger.info("转账失败，已恢复用户 {} 余额 {}",user_id,wallet.getAmount());
            json.setSattusCode(StatusCode.TRANSFER_ERROR);
            e.printStackTrace();
            return json;
        }catch (Exception e){
            wallet.setAmount(wallet.getAmount() + amount);
            wallet.setUpdateTime(new Date());
            walletService.updateByPrimaryKey(wallet);
            logger.info("转账失败，已恢复用户 {} 余额 {}",user_id,wallet.getAmount());
            json.setSattusCode(StatusCode.TRANSFER_ERROR);
            e.printStackTrace();
            return json;
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
            if(response.getSubCode().equals("20000")||response.getSubCode().equals("40004")||response.getSubCode().equals("SYSTEM_ERROR")){
                AlipayFundTransOrderQueryRequest orderQuery = new AlipayFundTransOrderQueryRequest();
                JSONObject query = new JSONObject();
                query.put("out_biz_no",out_biz_no);
                query.put("order_id",response.getOrderId());
                orderQuery.setBizContent(query.toJSONString());
                AlipayFundTransOrderQueryResponse queryResponse = null;
                try {
                    queryResponse = alipayClient.execute(orderQuery);
                } catch (AlipayApiException e) {
                    wallet.setAmount(wallet.getAmount() + amount);
                    wallet.setUpdateTime(new Date());
                    walletService.updateByPrimaryKey(wallet);
                    logger.info("转账失败，已恢复用户 {} 余额 {}",user_id,wallet.getAmount());
                    json.setSattusCode(StatusCode.TRANSFER_ERROR);
                    e.printStackTrace();
                    return json;
                }catch (Exception e){
                    wallet.setAmount(wallet.getAmount() + amount);
                    wallet.setUpdateTime(new Date());
                    walletService.updateByPrimaryKey(wallet);
                    logger.info("转账失败，已恢复用户 {} 余额 {}",user_id,wallet.getAmount());
                    json.setSattusCode(StatusCode.TRANSFER_ERROR);
                    e.printStackTrace();
                    return json;
                }
                if(!queryResponse.isSuccess()){
                    //查询失败，返回失败
                    wallet.setAmount(wallet.getAmount() + amount);
                    wallet.setUpdateTime(new Date());
                    walletService.updateByPrimaryKey(wallet);
                    logger.info("转账失败，已恢复用户 {} 余额 {}",user_id,wallet.getAmount());
                    json.setSattusCode(StatusCode.TRANSFER_ERROR);
                    return json;
                }
            }
                //成功，产生交易流水
                WalletDetail detail = new WalletDetail();
                detail.setOrderNo(out_biz_no);
                detail.setUserid(user_id);
                detail.setAmount(Float.valueOf("-"+amount));
                detail.setReason("提现");
                detail.setRemark("出账");
                detail.setTradeNo(response.getOrderId());
                detail.setCreateby(user_id+"");
                detail.setCreatetime(new Date());
                walletDetailService.insert(detail);
                logger.info("提现成功，生成交易记录：{}",user_id);
                json.setSuc();
                return json;


        } else {
            System.out.println("调用失败");
            wallet.setAmount(wallet.getAmount() + amount);
            wallet.setUpdateTime(new Date());
            walletService.updateByPrimaryKey(wallet);
            logger.info("转账失败，已恢复用户 {} 余额 {}",user_id,wallet.getAmount());
            json.setSattusCode(StatusCode.TRANSFER_ERROR);
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
        //钱包余额扣除
        wallet.setAmount(wallet.getAmount()-order.getAmount());
        walletService.updateByPrimaryKey(wallet);
        logger.info("退款成功，钱包已扣除余额：-{}元 ",order.getAmount());
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
                //钱包余额扣除
                wallet.setAmount(wallet.getAmount()+order.getAmount());
                walletService.updateByPrimaryKey(wallet);
                logger.info("退款成功，钱包已扣除余额：-{}元 ",order.getAmount());
                logger.info("退款失败，订单：{} ",res.getBody());
            }
        } catch (AlipayApiException e) {
            wallet.setAmount(wallet.getAmount()+order.getAmount());
            walletService.updateByPrimaryKey(wallet);
            logger.info("退款失败，订单：{} 已恢复余额 {}",res.getBody(),wallet.getAmount());
        }catch (Exception e){
            wallet.setAmount(wallet.getAmount()+order.getAmount());
            walletService.updateByPrimaryKey(wallet);
            logger.info("退款失败，订单：{} 已恢复余额 {}",order.getAmount(),wallet.getAmount());
        }
        json.setSattusCode(StatusCode.PAY_BACK_Error);
        return json;
    }






}
