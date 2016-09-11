package com.usee.service;

import com.usee.model.User;

public interface UserService {

	public User getUser(String id);

	public void addUser(User user);

	public boolean updateUser(User user);
	
	public boolean updateUser_Cellphone(User user);

	public boolean updateUser_OAuth(User user);

	public boolean changePassword(User user);
	
	public boolean modifyPassword(User user);
	
	public User getUserByOpenId(String tag, String openId);
	
	public User getUserByCellphone(String cellphone);
	
	public void feedback(String messages);
}
