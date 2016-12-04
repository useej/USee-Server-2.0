package com.usee.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usee.dao.UserDao;
import com.usee.model.User;
import com.usee.service.WechatRedirectService;
import com.usee.utils.URL2PictureUtil;
import com.usee.utils.UUIDGeneratorUtil;

import net.sf.json.JSONObject;

@Service
public class WechatRedirectServiceImpl implements WechatRedirectService {
	
	private static final String DEFAULT_CELLPHONE = "<dbnull>";
	private static final String DEFAULT_PASSWORD = "<dbnull>";
//	private static final int DEFAULT_GENDER = 2;	

	private static final String APPID = "";
	private static final String SECRET = "";
	
	private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	private static final String GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN ";
	
	@Autowired
	private UserDao userDao;
	
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
		
		System.out.println(url);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();  
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			String returnJson = EntityUtils.toString(httpEntity, "utf-8");
			JSONObject userInfoJson = JSONObject.fromObject(returnJson);
			user.setOpenID_wx(userInfoJson.getString("openid"));
			user.setNickname(userInfoJson.getString("nickname"));
			user.setGender(userInfoJson.getInt("sex"));
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
	
	@Override
	public void addUser(User user, String fileRootDir) {
		user.setUserID(UUIDGeneratorUtil.getUUID());
		user.setCreateTime(new Date().getTime() + "");
		
		// 将用户头像保存到本地图片服务器
		URL2PictureUtil.download(user.getUserIcon(), user.getUserID(), fileRootDir);
		user.setUserIcon(user.getUserID() + ".png");
		// 设置默认的手机号
		user.setCellphone(DEFAULT_CELLPHONE);
		// 设置默认的密码
		user.setPassword(DEFAULT_PASSWORD);
		user.setStatus("1");
		userDao.addUser_OAuth(user);
	}

}
