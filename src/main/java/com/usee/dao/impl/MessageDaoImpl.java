package com.usee.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usee.dao.MessageDao;
import com.usee.model.Comment;

@Service
public class MessageDaoImpl implements MessageDao {
	
	@Autowired
	SessionFactory sessionFactory;

	public int getNewMsgsNum(String userID, String latestReadTime) {
		String hql = "from Comment c where c.receiver = ? AND c.create_time > ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userID);
		query.setString(1, latestReadTime);
		return query.list().size();
	}

	public List<Comment> getNewMsgs(String userID, String latestReadTime) {
		String hql = "from Comment c where c.receiver = ? AND c.create_time > ? ORDER BY c.create_time DESC";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userID);
		query.setString(1, latestReadTime);
		return query.list();
	}

	public List<Comment> getallMsgsbyID(String userID) {
		String hql = "from Comment c where c.receiver = ? ORDER BY c.create_time DESC";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userID);
		return query.list();
	}

}
