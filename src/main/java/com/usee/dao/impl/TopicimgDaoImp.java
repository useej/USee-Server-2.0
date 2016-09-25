package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.TopicimgDao;
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

	@Override
	public List<String> gettopicimg(String topicID) {
		String hql = "select ti.imgurl from Topicimg ti where ti.topicid = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicID);
		return query.list();
	}

}
