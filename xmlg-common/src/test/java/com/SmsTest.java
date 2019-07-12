package com;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import net.sf.json.JSONObject;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-10-11:43
 */
public class SmsTest {
    public String smsUrl="https://webapi.sms.mob.com/sms/verify";
    public String smsParams="appkey=2baba04410948&zone=86&";
    @Test
    public void checkSms(){
        System.out.println(checkSMS("17318035749","8252"));
    }
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
