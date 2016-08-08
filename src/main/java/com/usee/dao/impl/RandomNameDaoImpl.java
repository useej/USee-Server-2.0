package com.usee.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.RandomNameDao;

@Service
public class RandomNameDaoImpl implements RandomNameDao {
	
	@Resource
	private SessionFactory sessionFactory;
	
	public String getRandomNameById(int id) {
		String sql = "SELECT nickname from nicknames where id = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, id);
		return (String) query.uniqueResult();
	}

}
