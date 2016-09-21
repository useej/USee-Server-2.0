package com.usee.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShareTopicUtil {
	
	public static String getShareURL() {
		String filePath = API.SHERA_TOPIC_PATH;
		String fileTxt = null;
		File file = new File(filePath);
		try {
			if(file.isFile() && file.exists()) {
				FileInputStream in = new FileInputStream(file);
				InputStreamReader read = new InputStreamReader(in);
				BufferedReader bufferedReader = new BufferedReader(read);
				while((fileTxt = bufferedReader.readLine()) != null) {
					return fileTxt;
				}
				read.close();
			} else {
				System.out.println("找不到文件");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileTxt;
	}

}
