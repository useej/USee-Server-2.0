package com.usee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.dao.UserDao;
import com.usee.model.User;
import com.usee.service.UserManager;

@Service
public class UserManagerImpl implements UserManager {
@Resource
private UserDao userDao;  

public void setUserDao(UserDao userDao) {  
    this.userDao = userDao;  
}  

public User getUser(String id) {  
    return userDao.getUser(id);  
}  

public List<User> getAllUser() {  
    return userDao.getAllUser();  
}  

public void addUser(User user) {  
    userDao.addUser(user);  
}  

public boolean delUser(String id) {  

    return userDao.delUser(id);  
}  

public boolean updateUser(User user) {  
    return userDao.updateUser(user);  
}  

}
