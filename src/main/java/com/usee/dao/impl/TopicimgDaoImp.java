package com.usee.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.TopicimgDao;
import com.usee.model.Topic;
import com.usee.model.Topicimg;

@Service
public class TopicimgDaoImp implements TopicimgDao {
	@Resource
	private SessionFactory sessionFactory;
	
	public void savetopicimg(Topicimg topicimg) {
		Session session = sessionFactory.getCurrentSession();
		session.save(topicimg);
		session.flush();
		
	}

}
