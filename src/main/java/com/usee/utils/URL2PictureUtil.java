package com.usee.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 使用URLConnection将用户头像下载到本地图片服务器
 */
public class URL2PictureUtil {
	
	// 定义保存图片的路径
	private static final String FILEDIR = "userIcons";
	// 定义保存图片的后缀
	private static final String SUFFIX = ".png";
	

	public static String download(String urlString, String fileName, String fileRootDir) {

		File file = new File(fileRootDir + FILEDIR);
		if (!file.exists() && !file.isDirectory())
			file.mkdir();
		System.out.println(fileRootDir + FILEDIR);

		URL url = null;
		URLConnection con = null;
		InputStream is = null;
		OutputStream os = null;

		try {
			// 构造URL
			url = new URL(urlString);
			// 打开连接
			con = url.openConnection();
			// 输入流
			is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len = 0;
			// 输出的文件流
			File f = new File(file, fileName + SUFFIX);
			os = new FileOutputStream(f);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName + "SUFFIX";
	}

}

