package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.DanmuDao;
import com.usee.moddel.Danmu;
import com.usee.moddel.User;
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
	public List<Danmu> getDanmuList(String topicId) {
		// TODO Auto-generated method stub
		String hql = "from Danmu m where m.topicId =?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		String sql="select * from danmu where topicid=?";
//		Query query=sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString(0, topicId);
//		System.out.println(query.list().size());
		return query.list();
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
