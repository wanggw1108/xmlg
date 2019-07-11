package com.temporary.center.ls_common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtil {
    /**
     * @param url
     * @return
     * @throws IOException
     */
    public static String send(String method, String url, String postStr, Map<String, String> headers) throws IOException {
        return send(method, url, postStr, headers, "utf-8");
    }

    public static String send(String method, String url, String postStr, Map<String, String> headers, String charset) throws IOException {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response = "";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod(method);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            if ("POST".equalsIgnoreCase(method)) {
                //POST请求
                out = new OutputStreamWriter(
                        conn.getOutputStream());
                out.write(postStr);
                out.flush();
            }
            //读取响应
            InputStream in = conn.getInputStream();
            byte[] bytes = new byte[0];
            bytes = new byte[in.available()];
            in.read(bytes);
            if (charset == null) {
                charset = "utf-8";
            }
            response = new String(bytes, charset);
            in.close();
            // 断开连接
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return response;
    }

    public static String getValueByReg(String content, String reg) {
        String value = "";
        if (null != content && !"".equals(content) && null != reg && !"".equals(reg)) {
            Pattern p = Pattern.compile(reg.substring(0), Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(content);
            if (m.find()) {
                if (m.groupCount() >= 1) {
                    value = m.group(1);
                } else {
                    value = m.group();
                }
            }
        }
        return value;
    }
}