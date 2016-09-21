package com.usee.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.ColorDao;

@Service
public class ColorDaoImpl implements ColorDao {
	
	@Resource
	private SessionFactory sessionFactory;
	
	public String getColorById(int id) {
		String sql = "SELECT code from colorHexCodes where id = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, id);
//		query.setCacheable(true);
		return (String) query.uniqueResult();
	}

}
