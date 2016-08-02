package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.UserTopicDao;
import com.usee.model.UserTopic;

@Service
public class UserTopicDaoImp implements UserTopicDao{
	@Resource
	private SessionFactory sessionFactory;
	
	public void saveUserTopic(UserTopic userTopic){
		sessionFactory.getCurrentSession().save(userTopic);
	}
	
	public UserTopic getUserTopic(int id){
		String hql = "from UserTopic ut";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (UserTopic) query.list();
	}
	
	public UserTopic getUniqueUserTopicbyUserIdandTopicId(String userId, String topicId){
		String hql = "from UserTopic ut where ut.userId = ? and ut.topicId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		query.setString(1, topicId);
		return (UserTopic)query.uniqueResult();
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

	public void updateUserTopic(String userId, String topicId, int randomIconId, int randomNameId,	String lastVisitTime, int frequency, String userIcon) {
		String hql = "update UserTopic ut set ut.randomIconID = :randomIconId, ut.randomNameID = :randomNameId, " +
				"ut.lastVisit_time = :lastVisitTime, ut.frequency = :frequency, ut.userIcon = :userIcon where ut.userId = :userId and ut.topicId = :topicId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("topicId", topicId);
		query.setParameter("randomIconId", randomIconId);
		query.setParameter("randomNameId", randomNameId);
		query.setParameter("lastVisitTime", lastVisitTime);
		query.setParameter("frequency", frequency);
		query.setParameter("userIcon", userIcon);
		query.executeUpdate();
	}
	
	public void updateUserTopicLVTandFrequency(String userId, String topicId,String lastVisitTime, int frequency) {
		String hql = "update UserTopic ut set ut.lastVisit_time = :lastVisitTime, ut.frequency = :frequency where ut.userId = :userId and ut.topicId = :topicId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("topicId", topicId);
		query.setParameter("lastVisitTime", lastVisitTime);
		query.setParameter("frequency", frequency);
		query.executeUpdate();
		
	}
}
