package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.UserDao;
import com.usee.model.User;

@Service
public class UserDaoImpl implements UserDao {
@Resource
private SessionFactory sessionFactory;  

//public void setSessionFactory(SessionFactory sessionFactory) {  
//    this.sessionFactory = sessionFactory;  
//}  

/**
 * 根据用户id查询用户
 */
public User getUser(String id) {  

    String hql = "from User u where u.id=?";  
    Query query = sessionFactory.getCurrentSession().createQuery(hql);  
    query.setString(0, id);  

    return (User)query.uniqueResult();  
}  

/**
 * 查询所有用户
 */
public List<User> getAllUser() {  

    String hql = "from User";  
    Query query = sessionFactory.getCurrentSession().createQuery(hql);  

    return query.list();  
}  

/**
 * 添加用户
 */
public void addUser(User user) {  
    sessionFactory.getCurrentSession().save(user);  
}  

/**
 * 根据用户id删除用户
 */
public boolean delUser(String id) {  

    String hql = "delete User u where u.id = ?";  
    Query query = sessionFactory.getCurrentSession().createQuery(hql);  
    query.setString(0, id);  

    return (query.executeUpdate() > 0);  
}  

/**
 * 编辑用户
 */
public boolean updateUser(User user) {  

    String hql = "update User u set u.userName = ?,u.age=? where u.id = ?";  
    Query query = sessionFactory.getCurrentSession().createQuery(hql);  
//    query.setString(0, user.getUserName());  
//    query.setString(1, user.getAge());  
//    query.setString(2, user.getId());  

    return (query.executeUpdate() > 0);  
}  

}
