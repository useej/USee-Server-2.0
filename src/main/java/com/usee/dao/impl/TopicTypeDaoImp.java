package com.usee.dao.impl;

import java.util.List;

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
		Session session = sessionFactory.getCurrentSession();
		session.save(newtopictype);
		session.flush();
	}

	@Override
	public void updateTopictype(TopicType topictype) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void delTypeOfTopic(String topicID) {
		// TODO Auto-generated method stub
		String hql = "delete TopicType tt where tt.topicID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicID);

//		return (query.executeUpdate() > 0);
	}

	@Override
	public String getTypeOfTopic(String topicID) {
		// TODO Auto-generated method stub
		String hql = "select typeID from TopicType where topicID=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicID);
		List<String> list = query.list();
		
		return list.toString();
	}


}
