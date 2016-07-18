package com.usee.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.usee.service.CellphoneValidateService;  
  
@Service
public class CellphoneValidateServiceImpl implements CellphoneValidateService {  
  
    // 发送短信  
    public String sendMessage(String mobile, String msg){  
        HttpPost post = new HttpPost(SMS_SEND_URI);  
        List<NameValuePair> list = new ArrayList<NameValuePair>();  
        
        list.add(new BasicNameValuePair("account", ACCOUNT));  
        list.add(new BasicNameValuePair("pswd", PASSWORD)); 
        list.add(new BasicNameValuePair("mobile", mobile));  
        list.add(new BasicNameValuePair("msg", msg));  
        list.add(new BasicNameValuePair("needstatus", NEED_STATUS));  
        list.add(new BasicNameValuePair("product", ""));  
        
        String result = null;
		result = executeMethod(post, list);  
        
        System.out.println("响应码：" + result + "，手机号：" + mobile + "信息：" + msg);  
        
        return result;
    } 
    
    
    private String executeMethod(HttpPost post, List<NameValuePair> list) {  
    	// 设置编码格式
        try {
			post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}  
        CloseableHttpClient httpClient = HttpClients.createDefault();  
        CloseableHttpResponse httpResponse = null;  
        String result = "-1";  
        try {  
            httpResponse = httpClient.execute(post);  
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {  
        } finally {  
            post.releaseConnection();  
            if(httpResponse != null)
				try {
					httpResponse.close();
					httpClient.close();  
				} catch (IOException e) {
					e.printStackTrace();
				}       
        }  
        return result;  
    }  
}  
