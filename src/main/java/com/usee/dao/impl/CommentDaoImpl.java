package com.usee.dao.impl;

import javax.annotation.Resource;

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
}
