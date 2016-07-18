package com.usee.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usee.dao.UserDao;
import com.usee.model.User;
import com.usee.service.OAuthLoginService;
import com.usee.utils.URL2JsonUtil;
import com.usee.utils.URL2PictureUtil;
import com.usee.utils.UUIDGeneratorUtil;

@Service
public class OAuthLoginServiceImpl implements OAuthLoginService {
	
	@Resource
	private UserDao userDao;
	
	// 调用QQAPI所需的信息
	public static String qq_appId = "xxx";
    public static String qq_baseUrl = "https://graph.qq.com";
    protected static final String QQ_URL_GET_USERINFO = qq_baseUrl
            + "/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";
    
    // 调用微博API所需的信息
    public static String weibo_baseUrl = "https://api.weibo.com";
    protected static final String WEIBO_URL_GET_USERINFO = weibo_baseUrl
            + "/2/users/show.json?access_token=%s&uid=%s";
    
    // 调用微信API所需的信息
    public static String weixin_baseUrl = "https://api.weixin.qq.com";
    protected static final String WEIXIN_URL_GET_USERINFO = qq_baseUrl
            + "/sns/userinfo?access_token=%s&openid=%s";
    
    /**
     * 调用地址：
     * https://graph.qq.com/user/get_user_info
     * 参数
     *   access_token=*************&
     *   oauth_consumer_key=12345&
     *   openid
     *   
     * }
     * 
     */
	@Override
	public User handleQQUserInfo(String access_token, String openid) {
	
        if (access_token.isEmpty() || openid.isEmpty()) {
            return null;
        }
        
        User user  = new User();
        
        // 将字符串组装成URL
        String url = String.format(QQ_URL_GET_USERINFO, access_token, qq_appId, openid);
        // 通过URL得到JSON
        String json = URL2JsonUtil.getJSON(url);
        
		try {
			// 使用Jackson得到map对象
			ObjectMapper mapper = new ObjectMapper();  
			Map<String,Object> map = new HashMap<String, Object>();
			map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
			// 筛选出需要的信息
			user.setNickName((String) map.get("nickname"));
			user.setUserIcon((String) map.get("figureurl_qq_1"));
		} catch (IOException e) {
			e.printStackTrace();
		}  
 
        user.setQqOpenId(openid);
        user.setCreateTime(new Date());
        user.setUserID(UUIDGeneratorUtil.getUUID());
        
        //将QQ头像保存到图片服务器
        URL2PictureUtil.download(user.getUserIcon(), user.getUserID());
		
        //将用户信息储存到数据库中
        userDao.addUser(user);
        
        return user;
	}
	
	/**
     * 获取用户信息(文档)
     * http://open.weibo.com/wiki/2/users/show
     * 
     * 调用地址：
     * https://api.weibo.com/2/users/show.json
     * 参数
     *   access_token=*************&
     *   uid
     * 
     */
	@Override
	public User handleWeiboUserInfo(String access_token, String uid) {
	
        if (access_token.isEmpty() || uid.isEmpty()) {
            return null;
        }
        
        User user  = new User();
        
        // 将字符串组装成URL
        String url = String.format(WEIBO_URL_GET_USERINFO, access_token, uid);
        // 通过URL得到JSON
        String json = URL2JsonUtil.getJSON(url);

		try {
			// 使用Jackson得到map对象
			ObjectMapper mapper = new ObjectMapper();  
			Map<String,Object> map = new HashMap<String, Object>();
			map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
			// 筛选出需要的信息
			user.setNickName((String) map.get("screen_name"));
			user.setUserIcon((String) map.get("avatar_hd"));
		} catch (IOException e) {
			e.printStackTrace();
		}  
 
		user.setWeiboOpenId(uid);
        user.setCreateTime(new Date());
        user.setUserID(UUIDGeneratorUtil.getUUID());
        
        //将微博头像保存到图片服务器
        URL2PictureUtil.download(user.getUserIcon(), user.getUserID());
		
        //将用户信息储存到数据库中
        userDao.addUser(user);
        
        return user;
	}
	
	 
    /**
     * 调用地址：
     * https://api.weixin.qq.com/sns/userinfo
     * 参数
     *   access_token=*************&
     *   openid
     *   
     */
	@Override
	public User handleWeinxinUserInfo(String access_token, String openid) {
	
        if (access_token.isEmpty() || openid.isEmpty()) {
            return null;
        }
        
        User user  = new User();
        
        // 将字符串组装成URL
        String url = String.format(WEIXIN_URL_GET_USERINFO, access_token, openid);
        // 通过URL得到JSON
        String json = URL2JsonUtil.getJSON(url);
        
		try {
			// 使用Jackson得到map对象
			ObjectMapper mapper = new ObjectMapper();  
			Map<String,Object> map = new HashMap<String, Object>();
			map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
			// 筛选出需要的信息
			user.setNickName((String) map.get("nickname"));
			user.setUserIcon((String) map.get("headimgurl"));
		} catch (IOException e) {
			e.printStackTrace();
		}  
 
        user.setWeixinOpenID(openid);
        user.setCreateTime(new Date());
        user.setUserID(UUIDGeneratorUtil.getUUID());
        
        //将微信头像保存到图片服务器
        URL2PictureUtil.download(user.getUserIcon(), user.getUserID());
		
        //将用户信息储存到数据库中
        userDao.addUser(user);
       
        return user;
	}
	

}
