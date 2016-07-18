package com.usee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.dao.UserDao;
import com.usee.model.User;
import com.usee.service.UserService;
import com.usee.utils.URL2PictureUtil;

@Service
public class UserServiceImpl implements UserService {
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

	public List<User> getAllUser() {
		return userDao.getAllUser();
	}

	public void addUser(User user) {
		userDao.addUser(user);
		// 将用户头像保存到本地图片服务器
		URL2PictureUtil.download(user.getUserIcon(), "这里填写头像名称");
	}

	public boolean delUser(String id) {
		return userDao.delUser(id);
	}

	public boolean updateUser(User user) {
		return userDao.updateUser(user);
	}

	
	public boolean changePassword(User user) {
		return userDao.changePassword(user);
	}

}
