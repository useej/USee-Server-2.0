package com.usee.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	
	//currentTime:格式为yyyy-MM-dd HH:mm:ss的当前时间
	public String currentTime = df.format(date); 
	
	//currentTimeStamp:UNIX时间戳，定义为从格林威治时间 1970 年 01 月 01 日 00 时 00 分 00 秒起至现在的总秒数。
	Long temp = new Long(System.currentTimeMillis()/1000);
	public String currentTimeStamp = temp.toString();
    
	/**
	 * 时间戳转成常见的yyyy-MM-dd HH:mm:ss型
	 * @param s:String类型unix时间戳，一般从数据库取出来的时候就是这种类型
	 * @return
	 */
    public String timestamp2Date(String s) {
    	Long timestamp = Long.parseLong(s) * 1000;  
    	return df.format(new Date(timestamp));  
    }
    
    /**
     * 常见的yyyy-MM-dd HH:mm:ss型转成unix时间戳，存入数据库的时候用到
     * @param s:常见yyyy-MM-dd HH:mm:ss格式的实践
     * @return
     * @throws ParseException
     */
    public String date2Timestamp(String s) throws ParseException{
		long l = df.parse(s).getTime();
		return String.valueOf(l).substring(0, 10);
    }
}
