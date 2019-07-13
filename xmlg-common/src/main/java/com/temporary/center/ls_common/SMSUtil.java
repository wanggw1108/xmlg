package com.temporary.center.ls_common;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.common.model.RegisterInfo;
import cn.jmessage.api.common.model.RegisterPayload;
import cn.jmessage.api.user.UserClient;
import cn.jmessage.api.user.UserInfoResult;
import cn.jmessage.api.user.UserStateResult;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-10-12:02
 */
@Component
public class SMSUtil {

    @Value("${jg_AppKey}")
    private String jgAppKey;

    @Value("${jg_masterSecret}")
    private String jgMasterSecret;
    @Value("${smsUrl}")
    private String smsUrl;
    @Value("${smsParams}")
    private String smsParams;
//    private static final LogUtil logger = LogUtil.getLogUtil(SMSUtil.class);
private static final Logger logger = LoggerFactory.getLogger(SMSUtil.class);



public boolean createJiGuangUser(String phone){

    UserClient userClient = new UserClient(jgAppKey, jgMasterSecret);
    try {
        List<RegisterInfo> users = new ArrayList<RegisterInfo>();
        RegisterInfo user = RegisterInfo.newBuilder()
                .setUsername(phone)
                .setPassword("123456")
                .setNickname(phone)
                .build();
        users.add(user);
        RegisterInfo[] regUsers = new RegisterInfo[users.size()];
        RegisterPayload payload = RegisterPayload.newBuilder()
                .addUsers(users.toArray(regUsers)).build();
        ResponseWrapper res = userClient.registerUsers(payload);
        return true;
    } catch (APIConnectionException e) {
        return false;
    } catch (APIRequestException e) {
        return false;
    }


}



    /**
     * 发起https 请求
     * @param phone
     * @param code
     * @return
     */
    public  int checkSMS(String phone,String code){

        StringBuilder builder = new StringBuilder(smsParams);
        builder.append("phone=")
                .append(phone)
                .append("&code=")
                .append(code);
        String params = builder.toString();
        HttpURLConnection conn = null;
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());

            //ip host verify
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return urlHostName.equals(session.getPeerHost());
                }
            };

            //set ip host verify
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            URL url = new URL(smsUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");// POST
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            // set params ;post params
            if (params!=null) {
                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(params.getBytes(Charset.forName("UTF-8")));
                out.flush();
                out.close();
            }
            conn.connect();
            //get result
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = conn.getInputStream();

                byte[]  bt= new byte[stream.available()];
                stream.read(bt);
                String result = new String(bt);
                logger.info("phone check result:"+phone+" "+result);
                JSONObject json = JSONObject.fromObject(result);

                return json.getInt("status");
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return -1;
    }

}
