package com.usee.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.UserDao;
import com.usee.model.User;

@Service
public class UserDaoImpl implements UserDao {
	@Resource
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 根据用户id查询用户
	 */
	public User getUser(String id) {

		String hql = "from User u where u.userID=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);

		return (User) query.uniqueResult();
	}


	/**
	 * 添加用户
	 */
	public Boolean addUser(User user) {
		String hql = "update User u set u.password = ?,u.nickname = ?,u.userIcon = ?,u.createTime = ? "
				+ "where u.userID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getPassword());
		query.setString(1, user.getNickname());
		query.setString(2, user.getUserIcon());
		query.setString(3, user.getCreateTime());
		query.setString(4, user.getUserID());

		return (query.executeUpdate() > 0);
	}

	/**
	 * 更新用户信息
	 */
	public boolean updateUser(User user) {

		String hql = "update User u set u.gender = ?,u.nickname = ?,u.userIcon = ? "
				+ "where u.userID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, user.getGender());
		query.setString(1, user.getNickname());
		query.setString(2, user.getUserIcon());
		query.setString(3, user.getUserID());

		return (query.executeUpdate() > 0);
	}

	/**
	 * 根据用户的openId得到用户信息
	 */
	public User getUserByOpenId(String tag, String openId) {
		String hql = "from User u where u." + tag + "=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, openId);

		return (User) query.uniqueResult();
	}

	/**
	 * 根据用户的cellphone得到用户信息
	 */
	public User getUserByCellphone(String cellphone) {
		String hql = "from User u where u.cellphone=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, cellphone);

		return (User) query.uniqueResult();
	}

	/**
	 * 修改用户密码
	 */
	public boolean changePassword(User user) {
		String hql = "update User u set u.password = ? "
				+ "where u.cellphone = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getPassword());
		query.setString(1, user.getCellphone());

		return (query.executeUpdate() > 0);
	}
	
	/**
	 * 解除绑定或者绑定第三方用户账号
	 */
	public boolean updateUser_OAuth(User user) {
		
		String hql = "update User u set u.openID_qq = ?,u.openID_wx = ?,u.openID_wb = ? "
				+ "where u.userID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getOpenID_qq());
		query.setString(1, user.getOpenID_wx());
		query.setString(2, user.getOpenID_wb());
		query.setString(3, user.getUserID());

		return (query.executeUpdate() > 0);
	}

	public boolean updateUser_Cellphone(User user) {
		
		String hql = "update User u set u.cellphone = ?,u.password = ? "
				+ "where u.userID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getCellphone());
		query.setString(1, user.getPassword());
		query.setString(2, user.getUserID());

		return (query.executeUpdate() > 0);
	}

	/**
	 * 修改用户密码
	 */
	public boolean modifyPassword(User user) {
		String hql = "update User u set u.password = ? "
				+ "where u.userID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getPassword());
		query.setString(1, user.getUserID());

		return (query.executeUpdate() > 0);
	}

	public void addUser_OAuth(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.save(user);
		session.flush();
	}

}
