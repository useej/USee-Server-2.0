package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.TopicDao;
import com.usee.model.Danmu;
import com.usee.model.Topic;

@Service
public class TopicDaoImpl implements TopicDao {

	@Resource
	private SessionFactory sessionFactory;

	public Topic getTopic(String id) {
		String hql = "from Topic t where t.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
//		query.setCacheable(true);
		return (Topic) query.uniqueResult();
	}

	public List<Topic> getAllTopic() {
		String hql = "from Topic order by lastDanmu_time desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		query.setCacheable(true);
		return query.list();
	}

	public void addTopic(Topic topic) {
		Session session = sessionFactory.getCurrentSession();
		session.save(topic);
		session.flush();
	}

	public boolean delTopic(String id) {
		String hql = "delete Topic t where t.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);

		return (query.executeUpdate() > 0);
	}

	public List getUserTopicsID(String userID) {
		// TODO Auto-generated method stub
		String hql ="select id from Topic where userID=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userID);
//		query.setCacheable(true);
  		return query.list();
	}

	/**
	 * 获取所有弹幕的Id
	 */
	public List getAllTopicId(){
		String hql ="select id from topic";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
		return query.list();
	}

	public List getUserTopics(String topicID) {
		String hql ="from Topic where id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicID);
//		query.setCacheable(true);
		return query.list();
	}


	public void updateUser_topic(String userID, String topicID) {
		// TODO Auto-generated method stub

	}

	public List searchTopic(String keyword) {
		String hql="from Topic  where title like '%"+keyword+"%'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

    public List<String> getTopicsbyDanmuNum(int num) {
        String sql="select DISTINCT title from topic ORDER BY danmuNum desc";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setFirstResult(0);
        query.setMaxResults(num);
        return query.list();
    }

	public String getTopicTitleForWeb(String topicID) {
		String hql = "select title from Topic t where t.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicID);
		return (String) query.uniqueResult();
	}

	public List<Topic> getTopicsbyType(String typeID) {
		String hql = "from Topic where id in(select topicID from TopicType where typeID=?)";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, typeID);
//		query.setCacheable(true);
		return query.list();
	}

	@Override
	public void updateType(String topicID, int type) {
		String hql = "update Topic set type=? where id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, type);
		query.setString(1, topicID);
		query.executeUpdate();
	}

	@Override
	public String getTopicIDBytitle(String topicTitle) {
		String hql = "select id from Topic t where t.title=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicTitle);
		return (String) query.uniqueResult();

	}

}
