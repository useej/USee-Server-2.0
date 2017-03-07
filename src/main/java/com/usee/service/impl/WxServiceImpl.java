package com.usee.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.usee.model.User;
import com.usee.service.WxService;

import net.sf.json.JSONObject;

@Service
public class WxServiceImpl implements WxService {
	private static final String APPID = "wxd3321fb7bc0c69e5";
	private static final String SECRET = "eed19265ee24481bea594734e860fd7f";
	
	private static final String REDIRECT_URL = "http://www.useeba.com/wechat/login/";
	private static final String GET_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s#wechat_redirect";
	private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	private static final String GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
	
	@Override
	public String getCodeUrl(String scope, String userId) {
		String redirect_url = REDIRECT_URL + scope;
		String get_code_url = String.format(GET_CODE_URL, APPID, redirect_url, "code", "snsapi_base", "STATE");;
		return get_code_url;
	}
	
	@Override
	public Map<String, String> getToken(String code) {
		Map<String, String> data = new HashMap<String, String>();
		String url = String.format(GET_TOKEN_URL, APPID, SECRET, code);
		
		System.out.println(url);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();  
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			String returnJson = EntityUtils.toString(httpEntity, "utf-8");
			
			
			System.out.println("返回信息--------> " + returnJson);
			
			JSONObject tokenJson = JSONObject.fromObject(returnJson);
			data.put("access_token", tokenJson.getString("access_token"));
			data.put("expires_in", tokenJson.getString("refresh_token"));
			data.put("refresh_token", tokenJson.getString("refresh_token"));
			data.put("openid", tokenJson.getString("openid"));
		} catch (Exception e) {		
			e.printStackTrace();
		} finally {  
			httpGet.releaseConnection();  
            if(httpResponse != null)
				try {
					httpResponse.close();
					httpClient.close();  
				} catch (IOException e) {
					e.printStackTrace();
				}       
        }  
		return data;
	}

	@Override
	public User getUserInfo(String access_token, String openid) {
		User user = new User();
		String url = String.format(GET_USERINFO_URL, access_token, openid);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();  
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			String returnJson = EntityUtils.toString(httpEntity, "utf-8");
			
			System.out.println("返回信息--------> " + returnJson);
			
			
			JSONObject userInfoJson = JSONObject.fromObject(returnJson);
			user.setOpenID_wx(userInfoJson.getString("openid"));
			user.setNickname(userInfoJson.getString("nickname"));
			if(userInfoJson.getInt("sex") == 2) {
				// 微信中用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
				// USee中用户的性别，值为1时是男性，值为0时是女性，值为2时是未知
				user.setGender(0);
			} else if (userInfoJson.getInt("sex") == 1) {
				user.setGender(1);
			} else {
				user.setGender(2);
			}
			
			user.setUserIcon(userInfoJson.getString("headimgurl"));
		} catch (Exception e) {		
			e.printStackTrace();
		} finally {  
			httpGet.releaseConnection();  
            if(httpResponse != null)
				try {
					httpResponse.close();
					httpClient.close();  
				} catch (IOException e) {
					e.printStackTrace();
				}       
        }  
		return user;
	}

}
