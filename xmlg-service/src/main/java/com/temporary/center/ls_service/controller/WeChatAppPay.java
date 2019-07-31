package com.temporary.center.ls_service.controller;


import com.itextpdf.text.pdf.qrcode.BitMatrix;
import com.temporary.center.ls_common.*;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.OrderMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.*;
/**
 * @author wangguowei
 * @description
 * @create 2019-07-31-13:23
 */
@Controller
@RequestMapping(value = "/wechat")
public class WeChatAppPay {


    private static final Logger logger = LoggerFactory.getLogger(WeChatAppPay.class);
    @Autowired
    OrderMapper orderService;
    @Autowired
    WeixinpayProperties weixinpayProperties;

    /***
     *发起支付
     * */

    /**
     * 支付请求 拉取微信预付单
     *
     * @throws Exception
     */
    @RequestMapping(value = "/pay.do", method = RequestMethod.POST)
    public Json weixinpay(String token, String orderId) throws Exception {


        Json json = new Json();
        // 获取订单号
        // 商品价格
        // BigDecimal goodsPrice = order.getGoodsPrice();
        // 订单总价
        // 微信开放平台审核通过的应用APPID
        System.out.println("appid是：" + weixinpayProperties.getAppid());
        System.out.println("mch_id是：" + weixinpayProperties.getMch_id());
        String nonce_str = System.currentTimeMillis()+"";
        System.out.println("随机字符串是：" + nonce_str);
        String body = "123456";
        String total_price = null;// 订单总金额，单位为分，详见支付金额
		/*
		 * double totalfee =0; try{
		 * totalfee=Double.parseDouble(total_price);////单位是分，即是0.01元 }catch (Exception
		 * e) { totalfee=0; }
		 */
        String spbill_create_ip; // 用户端实际ip
        int total_fee = (int) (1);
        spbill_create_ip = "127.0.0.1";// "127.0.0.1";
        System.out.println("spbill_create_ip===="+spbill_create_ip);
        String notify_url = weixinpayProperties.getNotify_url();
        System.out.println("notify_url是：" + notify_url);
        String trade_type = "APP";

        // 参数：开始生成签名
        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", weixinpayProperties.getAppid());
        parameters.put("mch_id", weixinpayProperties.getMch_id());
        parameters.put("nonce_str", nonce_str);
        parameters.put("body", body);
        orderId = "test"+ System.currentTimeMillis();
        parameters.put("out_trade_no", orderId);
        parameters.put("total_fee", total_fee);
        parameters.put("notify_url", notify_url);
        parameters.put("trade_type", trade_type);
        parameters.put("spbill_create_ip",spbill_create_ip);

        String sign = WXSignUtils.createSign(parameters,weixinpayProperties.getKey());
        System.out.println("签名是：" + sign);

        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setAppid(weixinpayProperties.getAppid());
        unifiedorder.setMch_id(weixinpayProperties.getMch_id());
        unifiedorder.setNonce_str(nonce_str);
        unifiedorder.setSign(sign);
        unifiedorder.setBody(body);
        unifiedorder.setOut_trade_no(orderId);
        unifiedorder.setTotal_fee(total_fee);
        unifiedorder.setSpbill_create_ip(spbill_create_ip);
        unifiedorder.setNotify_url(notify_url);
        unifiedorder.setTrade_type(trade_type);

        // 构造xml参数
        String xmlInfo = HttpXmlUtils.xmlInfo(unifiedorder);
        System.out.println("xmlInfo:" + xmlInfo);
        String method = "POST";
//        String weixinPost = HttpUtils.sentPost(weixinpayProperties.getUrl(),xmlInfo,"UTF-8");
        String weixinPost = HttpXmlUtils.xmlHttpProxy(weixinpayProperties.getUrl(), xmlInfo, "UTF-8").toString();// 请求微信

        System.out.println("weixinPost:" + weixinPost);

        UnifiedorderResult unifiedorderResult = ParseXMLUtils.jdomParseXml(weixinPost);// 解析微信的反馈

        if (unifiedorderResult != null) {

            if ("SUCCESS".equals(unifiedorderResult.getReturn_code())) {
                // 开始拼接App调起微信的参数
                SortedMap<String, Object> wxAppparameters = new TreeMap<String, Object>();
                wxAppparameters.put("appid", unifiedorderResult.getAppid());
                wxAppparameters.put("partnerid", unifiedorderResult.getMch_id());
                wxAppparameters.put("prepayid", unifiedorderResult.getPrepay_id());
                wxAppparameters.put("package", weixinpayProperties.getWx_package());
                wxAppparameters.put("noncestr", nonce_str);
                wxAppparameters.put("timestamp", String.valueOf(new Date().getTime()).substring(0, 10));
                wxAppparameters.put("sign", WXSignUtils.createSign( wxAppparameters,weixinpayProperties.getKey()));
                json.setData(wxAppparameters);
                json.setSuc();
                return json;
            } else {
                logger.error("错误原因为：" + unifiedorderResult.getReturn_msg());
                json.setSattusCode(StatusCode.Create_Order_Error);
                json.setMsg(unifiedorderResult.getReturn_msg());
                return json;
            }
        } else {
            logger.error("服务端请求微信的返回值异常。");
            json.setSattusCode(StatusCode.Create_Order_Error);
            return json;
        }

    }





    @RequestMapping(value = "/callback.do", method = RequestMethod.POST)
    public String notifyUrl(HttpServletRequest request) throws Exception {
        String result;//返回给微信的处理结果
        //BigDecimal platfor = null;
        logger.info("notifyUrl");
        // 读取参数
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, Object> m = new TreeMap<>();
        m = ParseXMLUtils.beginXMLParse(sb.toString());

        //过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator<String> it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = it.next();
            String parameterValue = (String)m.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            logger.info("p:" + parameter + ",v:" + v);
            packageParams.put(parameter, v);
        }

        // 微信支付的API密钥
        String key = weixinpayProperties.getKey();
        if (isTenpaySign("UTF-8", packageParams, key)) { // 验证通过
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                //这里面是你的业务逻辑处理
                // 通知微信已经收到消息，不要再给我发消息了，否则微信会8连击调用本接口
                result = setXML("SUCCESS", "OK");
                System.out.println(result+"===1");
                return result;
                }
            }
         else {
            result = setXML("fail","xml获取失败");
            System.out.println(result+"===2");
            return result;
        }
    result = setXML("fail","xml获取失败");
	System.out.println(result+"===3");
	return result;
}

    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }


    /**
     * 类型转换
     *
     * @author chenp
     * @param matrix
     * @return
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) == true ? 0xff000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * 通过返回IO流获取支付地址
     *
     * @param in
     * @return
     */
    @SuppressWarnings({ "unused", "unchecked" })
    private String getElementValue(InputStream in, String key) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(in);
        Element root = document.getRootElement();
        List<Element> childElements = root.elements();
        for (Element child : childElements) {
            if (key.equals(child.getName())) {
                return child.getStringValue();
            }
        }
        return null;
    }

    /**
     * 获取本机IP地址
     *
     * @return IP
     */
    public static String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

    /**
     * 微信支付签名算法sign
     */
    @SuppressWarnings("unused")
    private String getSign(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        String[] keyArr = (String[]) map.keySet().toArray(new String[map.keySet().size()]);// 获取map中的key转为array
        Arrays.sort(keyArr);// 对array排序
        for (int i = 0, size = keyArr.length; i < size; ++i) {
            if ("sign".equals(keyArr[i])) {
                continue;
            }
            sb.append(keyArr[i] + "=" + map.get(keyArr[i]) + "&");
        }
        sb.append("key=" + weixinpayProperties.getKey());
        String sign = string2MD5(sb.toString());
        return sign;
    }

    /***
     * MD5加码 生成32位md5码
     */
    private String string2MD5(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     *
     * @return boolean
     */
    @SuppressWarnings("rawtypes")
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams,
                                       String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);

        // 算出摘要
        String mysign = MD5Utils.MD5Encoding(sb.toString()).toLowerCase();
        String tenpaySign = ((String) packageParams.get("sign")).toLowerCase();

        return tenpaySign.equals(mysign);
    }


}