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
	
	public String getRealnameInfo(String userID);
	
	
	public void addUser_OAuth_UserInfo(User user, String fileRootDir);
	
	public String addUser_OAuth_Base(String openId);
	
	public void updateUser_OAuth_UserInfo(User user, String fileRootDir);
}
