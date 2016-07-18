package com.usee.service;

import java.util.List;

import com.usee.model.User;

public interface UserService {

	public User getUser(String id);

	public List<User> getAllUser();

	public void addUser(User user);

	public boolean delUser(String id);

	public boolean updateUser(User user);

	public boolean changePassword(User user);
	
	public User getUserByOpenId(String tag, String openId);
	
	public User getUserByCellphone(String cellphone);
}
