package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.model.UserTopic;

@Service
public class UserTopicDaoImp {
	@Resource
	private SessionFactory sessionFactory;
	
	public void saveUserTopic(UserTopic userTopic){
		sessionFactory.getCurrentSession().save(userTopic);
	}
	
	public List<UserTopic> getUserTopicbyUserId(String userId){
		String hql = "from UserTopic ut where ut.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		return query.list();
	}
	
	public int getLatestFrequency(){
		String sql = "SELECT MAX(frequency) FROM user_topic";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		if(query.uniqueResult() == null){
			return 0;
		}
		else{
			return (Integer) query.uniqueResult();
		}
	}
}
