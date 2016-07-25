package com.usee.dao.impl;

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
	
//	public void updateUserTopic(String userId, String lastVisit_time, int frequency, String userIcon){
//		String sql = "UPDATE user_topic SET lastVisit_time = '" + lastVisit_time + "', frequency = " + frequency + ", userIcon = '" + userIcon + " WHERE userID = '" + userId + "'";
//		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
//	}
	
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
