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
//		query.setCacheable(true);
		return (Comment)query.uniqueResult();
	}

	public List<Comment> getCommentbyDanmuId(int danmuId) {
		String hql = "from Comment c where c.danmuId = ? and c.status <> 0";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, danmuId);
//		query.setCacheable(true);
		return query.list();
	}

	public List<String> getCommentSenderbyDanmuId(int danmuId) {
		String hql = "select c.sender from Comment c where c.danmuId = ? and c.status <> 0";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, danmuId);
		return query.list();
	}

	public int getLatestCommentIdByUserIdAndDanmuId(String sender, int danmuId) {
		String sql = "SELECT MAX(id) FROM comment where sender = ? and danmuId = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString(0, sender);
		query.setInteger(1, danmuId);
		if(query.uniqueResult() == null){
			return -1;
		}
		else{
			return (Integer) query.uniqueResult();
		}
	}

    @Override
    public Comment getCommentbyReplycommentId(int replycommentId) {
        String hql = "from Comment c where c.id = ? and c.status <> 0";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0, replycommentId);
        return (Comment) query.uniqueResult();
    }

    @Override
    public List<Comment> getCommentbyDanmuIdandCreatetime(int danmuId, String createTime) {
        String hql = "from Comment c where c.danmuId = ? and c.create_time > ? and c.status <> 0";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0, danmuId);
        query.setString(1, createTime);
//        query.setCacheable(true);
        return query.list();
    }

	@Override
	public Comment getCommentByIdAndSender(int commentID, String sender) {
		String hql = "from Comment c where c.id = ? and c.sender = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, commentID);
		query.setString(1, sender);
		return (Comment)query.uniqueResult();
	}

	@Override
	public List<Comment> getReplyCommentListbyReplycommentId(int replycommentId) {
		String hql = "from Comment c where c.reply_commentId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0, replycommentId);
		return query.list();
	}

	@Override
	public boolean deleteComment(int commentID) {
		String sql = "delete from comment where id = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, commentID);
		if(query.executeUpdate() != 0)
		{
			return true;
		}
		else{
			return false;
		}
	}
}
