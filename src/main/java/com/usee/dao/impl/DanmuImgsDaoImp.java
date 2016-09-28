package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.DanmuImgsDao;
import com.usee.model.DanmuImgs;

@Service
public class DanmuImgsDaoImp implements DanmuImgsDao {
	@Resource
	private SessionFactory sessionFactory;
	
	public void saveDanmuImgs(DanmuImgs DanmuImgs) {
		Session session = sessionFactory.getCurrentSession();
		session.save(DanmuImgs);
		session.flush();
		
	}

	@Override
	public List<String> getDanmuImgs(int danmuID) {
		String hql = "select di.imgurl from DanmuImgs di where di.danmuID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, danmuID);
		return query.list();
	}

}
