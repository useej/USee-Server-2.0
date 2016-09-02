package com.usee.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.usee.service.SensitiveWordService;
import com.usee.utils.API;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {

	private static String SENSITIVE_WORD_PATH = API.SENSITIVE_WORD_PATH;
	private static String SENSITIVE_WORD_URL = API.SENSITIVE_WORD_URL;
	
	public String filter(String message) {
		File file = new File(SENSITIVE_WORD_PATH);
		InputStream input;
		BufferedReader reader;
		try {
			input = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(input));
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				temp = temp.trim();
				if(message.contains(temp)) {
					message = message.replace(temp, "***");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

//	public static void main(String[] args) {
//		SensitiveWordServiceImpl s = new SensitiveWordServiceImpl();
//		System.out.println(s.filter("第一代领导"
//				+ "第二代领导"
//				+ "第三代领导"
//				+ "第四代领导"
//				+ "第五代领导"
//				+ "第六代领导"
//				));
//		
//		
//		File file = new File("E:\\USee\\aa.txt");
//		InputStream input;
//		BufferedReader reader;
//		try {
//			input = new FileInputStream(file);
//			reader = new BufferedReader(new InputStreamReader(input));
//			String temp = null;
//			while ((temp = reader.readLine()) != null) {
//				temp = temp.trim();
//				temp = temp.replace("\\u000d\\u000a", "," + "\n");
//				File f = new File("E:\\USee\\bb.txt");
//				f.createNewFile();
//				OutputStream out = new FileOutputStream(f);
//				BufferedWriter b = new BufferedWriter(new OutputStreamWriter(out));
//				b.write(temp);
//				b.close();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public Map<String, String> getSWFileInfo() {
		Map<String, String> returnMap = new HashMap<String, String>();
		File file = new File(SENSITIVE_WORD_PATH);
		String lastModified = file.lastModified() + "";
		returnMap.put("lastModified", lastModified);
		returnMap.put("fileURL", SENSITIVE_WORD_URL);
		return returnMap;
	}
}
