package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.TopicDao;
import com.usee.model.Topic;

@Service
public class TopicDaoImpl implements TopicDao {
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Override
	public Topic getTopic(String id) {
		String hql = "from Topic t where t.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);

		return (Topic) query.uniqueResult();
	}

	@Override
	public List<Topic> getAllTopic() {
		String hql = "from Topic";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);

		return query.list();
	}

	@Override
	public void addTopic(Topic topic) {
		sessionFactory.getCurrentSession().save(topic);
	}

	@Override
	public boolean delTopic(String id) {
		String hql = "delete Topic t where t.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);

		return (query.executeUpdate() > 0);
	}

}
