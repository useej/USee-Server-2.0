package com.usee.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * 使用URLConnection获取JSON数据
 */
public class URL2JsonUtil {

	public static String getJSON(String urlString) {

		String json = null;
		StringBuffer sb = new StringBuffer();
		
		URL url = null;
		URLConnection con = null;
		InputStreamReader isr = null;

		try {
			// 构造URL
			url = new URL(urlString);
			// 打开连接
			con = url.openConnection();
			// 输入流
			isr = new InputStreamReader(con.getInputStream());
			// 1K的数据缓冲
			char[] buffer = new char[1024];
			// 读取到的数据长度
			int len = 0;
			// 开始读取
			while ((len = isr.read(buffer)) != -1) {
				sb.append(buffer,0,len);
			}
			// 完毕，关闭所有链接
			isr.close();
			json = sb.toString();
			System.out.println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return json;
	}

}
