package com.temporary.center.ls_common;


import java.io.StringReader;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;


/**
 * @author wangguowei
 * @description
 * @create 2019-07-31-17:36
 */
public class ParseXMLUtils {

    private static Logger log = LoggerFactory.getLogger(ParseXMLUtils.class);

    /**
     * 1、DOM解析
     */
    @SuppressWarnings("rawtypes")
    public static Map<String,Object> beginXMLParse(String xml){
        Document doc = null;
        Map<String,Object> params = new TreeMap<>();
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML

            Element rootElt = doc.getRootElement(); // 获取根节点smsReport
            Iterator<Element> iterator = rootElt.elementIterator();

            while(iterator.hasNext()){
                Element e = iterator.next();
                Object data = e.getData();
                String name = e.getName();
                params.put(name,data);

            }
            return params;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }


    public static void main(String[] args){
        String xml = "<xml><appid><![CDATA[wx5c02d2fc37993a8b]]></appid><mch_id><![CDATA[154599805]]></mch_id><nonce_str><![CDATA[7465076292326296698]]></nonce_str><sign><![CDATA[144B9A942300AE6BFA2651EFF72C524A]]></sign><body><![CDATA[测试微信支付0.01]]></body><out_trade_no><![CDATA[test1564568568347]]></out_trade_no><total_fee><![CDATA[1]]></total_fee><spbill_create_ip><![CDATA[127.0.0.1]]></spbill_create_ip><notify_url><![CDATA[http://www.baidu.com]]></notify_url><trade_type><![CDATA[APP]]></trade_type></xml>";
        System.out.println(beginXMLParse(xml));
    }

    /**
     * 2、DOM4j解析XML（支持xpath）
     * 解析的时候自动去掉CDMA
     * @param xml
     */
    public static void xpathParseXml(String xml){
        try {
            StringReader read = new StringReader(xml);
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(read);
            String xpath ="/xml/appid";
            System.out.print(doc.selectSingleNode(xpath).getText());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 3、JDOM解析XML
     * 解析的时候自动去掉CDMA
     * @param xml
     */
    @SuppressWarnings("unchecked")
    public static UnifiedorderResult jdomParseXml(String xml){
        UnifiedorderResult unifieorderResult = new UnifiedorderResult();
        try {
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document
            org.jdom.Document doc;
            doc = (org.jdom.Document) sb.build(source);

            org.jdom.Element root = doc.getRootElement();// 指向根节点
            List<org.jdom.Element> list = root.getChildren();

            if(list != null && list.size() > 0){
                boolean flag1 = true;
                boolean flag2 = true;
                for (org.jdom.Element element : list) {
                    log.info("key是："+element.getName()+"，值是："+element.getText());

                    if("return_code".equals(element.getName())){
                        if("FAIL".equals(element.getText())){
                            flag1 = false;
                        }else{
                            unifieorderResult.setReturn_code(element.getText());
                        }
                    }

                    if("return_msg".equals(element.getName())){
                        if(element.getText() != null && !"OK".equals(element.getText())){//微信支付的第一个坑，这里返回了OK，23333
                            log.error("统一下单参数有误，错误原因为:"+element.getText());
                            return null;
                        }
                    }

                    if(flag1){
                        if("appid".equals(element.getName())){
                            unifieorderResult.setAppid(element.getText());
                        }
                        if("mch_id".equals(element.getName())){
                            unifieorderResult.setMch_id(element.getText());
                        }
                        if("nonce_str".equals(element.getName())){
                            unifieorderResult.setNonce_str(element.getText());
                        }
                        if("sign".equals(element.getName())){
                            unifieorderResult.setSign(element.getText());
                        }
                        if("err_code".equals(element.getName())){
                            unifieorderResult.setErr_code(element.getText());
                        }
                        if("err_code_des".equals(element.getName())){
                            unifieorderResult.setErr_code_des(element.getText());
                        }
                        if("result_code".equals(element.getName())){
                            if("FAIL".equals(element.getText())){
                                flag2 = false;
                                log.error("统一下单业务结果有误，无法返回预支付交易会话标识");
                            }else{
                                unifieorderResult.setResult_code(element.getText());
                            }
                        }
                    }
                    if(flag1 && flag2 && flag2 == true){
                        if("trade_type".equals(element.getName())){
                            unifieorderResult.setTrade_type(element.getText());
                        }
                        if("prepay_id".equals(element.getName())){
                            log.info("统一下单接口成功返回预支付交易会话标识！");
                            unifieorderResult.setPrepay_id(element.getText());
                        }
                    }

                }
                return unifieorderResult;
            }else{
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean parseInt(String key){
        if(!StringUtils.isEmpty(key)){
            if(key.equals("total_fee")||key.equals("cash_fee")||key.equals("coupon_fee")||key.equals("coupon_count")||key.equals("coupon_fee_0")){
                return true;
            }
        }

        return false;
    }


}
