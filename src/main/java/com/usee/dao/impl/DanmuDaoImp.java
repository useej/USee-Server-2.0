package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.DanmuDao;
import com.usee.model.Danmu;
import com.usee.model.User;
@Service
public class DanmuDaoImp implements DanmuDao {
	@Resource
	private SessionFactory sessionFactory;

	@Override
	public void saveDanmu(Danmu message) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(message);

	}

	@Override
	public Danmu getDanmu(String id) {
		// TODO Auto-generated method stub
		String hql = "from Danmu m where m.id =?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		return (Danmu)query.uniqueResult();  
	}
	
	@Override
	public List<Danmu> getDanmuList(String topicId) {
		// TODO Auto-generated method stub
		String hql = "from Danmu m where m.topicId =?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicId);
		return query.list();
	}

	@Override
	public List<Danmu> getDanmuList(String topicId, int pageNum,
			int pageSize) {
				String hql = "from Danmu m where m.topicId =?";
				Query query = sessionFactory.getCurrentSession().createQuery(hql);
				query.setString(0, topicId);
				query.setFirstResult(pageNum*pageSize);
				query.setMaxResults(pageSize);
				return query.list();
	}
}
