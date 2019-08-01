package com.temporary.center.ls_common;


import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-31-17:32
 */
public class WXSignUtils {

    /**
     * 微信支付签名算法sign
     * @param parameters
     * @return
     */
    public static String createSign(SortedMap<String,Object> parameters,String key){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" +key); //这里是商户那里设置的key);
                System.out.println("签名字符串:"+sb.toString());
//        System.out.println("签名MD5未变大写：" + MD5Util.MD5Encode(sb.toString(), characterEncoding));
        String sign = MD5Utils.getMD5(sb.toString()).toUpperCase();
        return sign;
    }


}
