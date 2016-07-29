package com.usee.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usee.dao.CellphoneValidateDao;
import com.usee.model.User;

@Service
public class CellphoneValidateDaoImple implements CellphoneValidateDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/*
	 * 通过手机号得到用户信息
	 * @see com.usee.dao.CellphoneValidateDao#getUserByCellphone(java.lang.String)
	 */
	public User getUserByCellphone(String cellphone) {
		String hql = "from User u where u.cellphone=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, cellphone);
		
		return (User) query.uniqueResult();
	}

	/*
	 * 更新用户的验证码
	 * @see com.usee.dao.CellphoneValidateDao#updateValidateCode(com.usee.model.User)
	 */
	public boolean updateValidateCode(User user) {
		String hql = "update User u set u.verificationCode = ?,u.vcSendTime = ? "
				+ "where u.cellphone = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getVerificationCode());
		query.setString(1, user.getVcSendTime());
		query.setString(2, user.getCellphone());

		return (query.executeUpdate() > 0);
	}

	/*
	 * 保存用户的验证码
	 * @see com.usee.dao.CellphoneValidateDao#saveValidateCode(com.usee.model.User)
	 */
	public void saveValidateCode(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.save(user);
		session.flush();
		System.out.println(user.toString());
	}
	

}
