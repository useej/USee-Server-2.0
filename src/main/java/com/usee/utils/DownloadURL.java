package com.usee.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class DownloadURL {
	 public static final String FILEPATH = API.PROJECT_PATH + "res/downloadURL";

	    public static String getDownloadURL() {
	        String lineTxt = null;
	        try {
	            String encoding = "UTF-8";
	            File file = new File(FILEPATH);
	            if (file.isFile() && file.exists()) { //判断文件是否存在
	                InputStreamReader read = new InputStreamReader(
	                        new FileInputStream(file), encoding);//考虑到编码格式
	                BufferedReader bufferedReader = new BufferedReader(read);

	                while ((lineTxt = bufferedReader.readLine()) != null) {
	                    //System.out.println(lineTxt);
	                    return lineTxt;
	                }
	                read.close();
	            } else {
	                System.out.println("找不到指定的文件");
	            }
	        } catch (Exception e) {
	            System.out.println("读取文件内容出错");
	            e.printStackTrace();
	        }
	        return lineTxt;
	    }
}
