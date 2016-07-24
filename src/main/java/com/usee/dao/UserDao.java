package com.usee.dao;

import com.usee.model.User;

public interface UserDao {

	public User getUser(String id);

	public Boolean addUser(User user);
	
	public void addUser_OAuth(User user);

	public boolean updateUser(User user);
	
	public boolean updateUser_Cellphone(User user);
	
	public boolean updateUser_OAuth(User user);
	
	public boolean changePassword(User user);
	
	public boolean modifyPassword(User user);
	
	public User getUserByOpenId(String tag, String openId);
	
	public User getUserByCellphone(String cellphone);
}
