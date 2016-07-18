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

		String hql = "delete User u where u.userID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);

		return (query.executeUpdate() > 0);
	}

	/**
	 * 编辑用户
	 */
	public boolean updateUser(User user) {

		String hql = "update User u set u.gender = ?,u.nickName = ?,u.userIcon = ?,u.cellphone = ?,u.password = ? "
				+ "where u.userID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, user.getGender());
		query.setString(1, user.getNickName());
		query.setString(2, user.getUserIcon());
		query.setString(3, user.getCellphone());
		query.setString(4, user.getPassword());
		query.setString(5, user.getUserID());

		return (query.executeUpdate() > 0);
	}

	/**
	 * 根据用户的openId得到用户信息
	 */
	@Override
	public User getUserByOpenId(String tag, String openId) {
		String hql = "from User u where u." + tag + "=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, openId);

		return (User) query.uniqueResult();
	}

	/**
	 * 根据用户的cellphone得到用户信息
	 */
	@Override
	public User getUserByCellphone(String cellphone) {
		String hql = "from User u where u.cellphone=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, cellphone);

		return (User) query.uniqueResult();
	}

	@Override
	public boolean changePassword(User user) {
		String hql = "update User u set u.password = ? "
				+ "where u.cellphone = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getPassword());
		query.setString(1, user.getCellphone());

		return (query.executeUpdate() > 0);
	}

}
