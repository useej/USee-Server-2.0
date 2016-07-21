package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.DanmuDao;
import com.usee.model.Danmu;
@Service
public class DanmuDaoImp implements DanmuDao {
	@Resource
	private SessionFactory sessionFactory;

	
	public void saveDanmu(Danmu danmu) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		session.save(danmu);
		session.flush();
	}

	
	public Danmu getDanmu(String id) {
		// TODO Auto-generated method stub
		String hql = "from Danmu d where d.id =?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		return (Danmu)query.uniqueResult();  
	}
	
	
	public List<Danmu> getDanmuList(String topicId) {
		// TODO Auto-generated method stub
		String hql = "from Danmu d where d.topicId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicId);
		return query.list();
	}

	
	public List<Danmu> getDanmuList(String topicId, int pageNum, int pageSize) {
		String hql = "from Danmu d where d.topicId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicId);
		query.setFirstResult((pageNum - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.list();
	}
	
	
	public List<Object[]> getDanmuDetails(String danmuId){
		//String hql = "from Danmu as d, User as u where d.userId = u.userId and d.id = ?";
		String sql = "SELECT d.id AS danmuid, devid, d.userID AS userid, status, topicID, lon, lat, praisenum, downnum, commentnum, hitnum, d.create_time AS dcreatetime, address, delete_time, head, messages, gender, nickname, userIcon, cellphone, password, c.id AS commontid, sender, receiver, content, c.reply_commentID AS replycommontid, type, c.create_time AS ccreatetime FROM danmu d LEFT JOIN user u ON d.userID = u.userID LEFT JOIN comment c ON d.id = c.danmuID WHERE d.id = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setResultTransformer(org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP);  
		query.setString(0, danmuId);
		List<Object[]> lobj = query.list();
		return lobj;
	}
	
	public String getLatestDanmiId(){
		String sql = "SELECT MAX(id) FROM danmu";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		return query.uniqueResult().toString();
	}
	
	public boolean userIdCheck(String userId){
		String sql = "SELECT count(*) FROM `user` WHERE userID = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		if(query.uniqueResult().toString() != "0"){
			return true;
		}
		else{
			return false;
		}
	}
}
