package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.CommentDao;
import com.usee.model.Comment;

@Service
public class CommentDaoImpl implements CommentDao{
	@Resource
	private SessionFactory sessionFactory;
	
	public void saveComment(Comment comment){
		Session session = sessionFactory.getCurrentSession();
		session.save(comment);
		session.flush();
	}

	public Comment getComment(int id) {
		String hql = "from Comment c where c.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, id);
		return (Comment)query.uniqueResult();
	}

	public List<Comment> getCommentbyDanmuId(int danmuId) {
		String hql = "from Comment c where c.danmuId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, danmuId);
		return query.list();
	}

	public List<String> getCommentSenderbyDanmuId(int danmuId) {
		String hql = "select c.sender from Comment c where c.danmuId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, danmuId);
		return query.list();
	}
}
