package com.temporary.center.ls_common;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.Base64;

/**
 * BASE64加密解密
 */
@SuppressWarnings("restriction")
public class BASE64 {

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	public static void main(String[] args) throws Exception {

		System.out.println(Base64.getEncoder().encodeToString("100862".getBytes()));
		String data = BASE64.encryptBASE64("100862".getBytes());
		System.out.println("加密前：" + data);

		byte[] byteArray = BASE64.decryptBASE64("aGwuMTcuc2cyLnRnLmxiLnNpbmFub2RlLmNvbQ==");
		System.out.println("解密后：" + new String(byteArray, "GBK"));
	}

}
