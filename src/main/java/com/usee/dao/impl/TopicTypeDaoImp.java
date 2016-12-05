package com.usee.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.TopicTypeDao;
import com.usee.model.TopicType;

@Service
public class TopicTypeDaoImp implements TopicTypeDao{
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public void addTopictype(TopicType newtopictype) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		session.save(newtopictype);
		session.flush();
//		String hql = "insert into topic_type values(0, ?, ?)";
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		query.setString(0, newtopictype.getTopicid());
//		query.setInteger(1, newtopictype.getTypeid());

//		query.executeUpdate();
	}

	@Override
	public void updateTopictype(TopicType topictype) {
		// TODO Auto-generated method stub
		
	}

}
