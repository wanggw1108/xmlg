package com.temporary.center.ls_common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Base64;

public class Base64ImgUtil
{
	private static Logger logger = LoggerFactory.getLogger(Base64ImgUtil.class);
	
	/**
	 * 将图片进行base64编码方法
	 * 
	 * @param data
	 * @return 返回图片的base64
	 */
	public String getImageBase64(byte[] data) {
		if(data == null)
			return null;
		
		return Base64.getEncoder().encodeToString(data);
	}
	
	/**
	 * 将图片进行base64编码方法
	 * 
	 * @param input
	 * @return 返回图片的base64
	 */
	public String getImageBase64(InputStream input)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		try
		{
			while (-1 != (n = input.read(buffer)))
			{
				output.write(buffer, 0, n);
			}
			
			byte[] bytes = output.toByteArray();

			return Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally {
			if(output != null)
				try
				{
					output.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
		}
		
		return null;
	}
	/**
	 * 将图片进行获取byte
	 *
	 * @param input
	 * @return 返回图片的base64
	 */
	public byte[]  getImageByte(InputStream input)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		try
		{
			while (-1 != (n = input.read(buffer)))
			{
				output.write(buffer, 0, n);
			}

			byte[] bytes = output.toByteArray();

			return bytes;
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally {
			if(output != null)
				try
				{
					output.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
		}

		return null;
	}

	
}
