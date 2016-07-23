package com.usee.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.dao.UserDao;
import com.usee.model.User;
import com.usee.service.UserService;
import com.usee.utils.MD5Util;
import com.usee.utils.UUIDGeneratorUtil;

@Service
public class UserServiceImpl implements UserService {
	private String default_nickname;
	private static final String DEFAULT_USERICON = "randomIcons\\default_usericon.png";
	private static final String DEFAULT_CELLPHONE = "usee";
	private String default_password;
	
	@Resource
	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User getUser(String id) {
		return userDao.getUser(id);
	}

	
	public User getUserByOpenId(String tag, String openId) {
		return userDao.getUserByOpenId(tag, openId);
	}

	
	public User getUserByCellphone(String cellphone) {
		return userDao.getUserByCellphone(cellphone);
	}


	public void addUser(User user) {
		user.setUserID(UUIDGeneratorUtil.getUUID());
		user.setCreateTime(new Date().getTime() + "");
		
		// 设置默认的昵称
		if(user.getNickname() == null) {
			default_nickname = UUIDGeneratorUtil.getUUID().substring(0, 10).toLowerCase();
			user.setNickname(default_nickname);
		}
		// 设置默认的头像
		if(user.getUserIcon() == null) {
			user.setUserIcon(DEFAULT_USERICON);
		}
		// 设置默认的手机号
		if(user.getCellphone() == null) {
			user.setCellphone(DEFAULT_CELLPHONE);
		}
		// 设置默认的密码
		if(user.getPassword() == null) {
			default_password = UUIDGeneratorUtil.getUUID();
			user.setPassword(default_password);
		} else {
			// 密码使用MD5加密
			String md5Password = MD5Util.getMD5(user.getPassword());
			user.setPassword(md5Password);
		}
		
		userDao.addUser(user);
	
	}

	/*
	 * 更新用户信息
	 * @see com.usee.service.UserService#updateUser(com.usee.model.User)
	 */
	public boolean updateUser(User user) {
		return userDao.updateUser(user);
	}

	
	public boolean changePassword(User user) {
		// 密码使用MD5加密
		String md5Password = MD5Util.getMD5(user.getPassword());
		user.setPassword(md5Password);
		
		return userDao.changePassword(user);
	}

	public boolean updateUser_OAuth(User user) {
		return userDao.updateUser_OAuth(user);
	}

	public boolean updateUser_Cellphone(User user) {
		// 将密码使用MD5加密
		user.setPassword(MD5Util.getMD5(user.getPassword()));;
		return userDao.updateUser_Cellphone(user);
	}

	public boolean modifyPassword(User user) {
		// 密码使用MD5加密
		String md5Password = MD5Util.getMD5(user.getPassword());
		user.setPassword(md5Password);
		
		return userDao.modifyPassword(user);
	}
	

}
