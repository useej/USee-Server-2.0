package com.usee.dao;

import java.util.List;

import com.usee.moddel.User;


public interface UserDao {

	public User getUser(String id);

	public List<User> getAllUser();

	public void addUser(User user);

	public boolean delUser(String id);

	public boolean updateUser(User user);
}
