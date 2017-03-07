package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.WxRoomeDao;
import com.usee.model.WxRoom;
import com.usee.model.WxDanmu;

@Service
public class WxRoomDaoImpl implements WxRoomeDao {

	@Resource
	private SessionFactory sessionFactory;
	
	@Override
	public WxRoom getWxRoom(int id) {
		String hql = "from WxRoom l where l.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, id);
		return (WxRoom) query.uniqueResult();
	}

	@Override
	public int addWxRoom(WxRoom wxRoom) {
		Session session = sessionFactory.getCurrentSession();
		session.save(wxRoom);
		session.flush();
		return wxRoom.getId();
	}

	@Override
	public int saveDanmu(WxDanmu wxDanmu) {
		Session session = sessionFactory.getCurrentSession();
		session.save(wxDanmu);
		session.flush();
		return wxDanmu.getId();
	}

	@Override
	public List<WxDanmu> getDanmuByWxRoomId(int wxRoomId) {
		String hql = "from WxDanmu ld where ld.wxRoomId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, wxRoomId);
		return query.list();
	}

	@Override
	public WxRoom getWxRoomByOwnerId(String ownerId) {
		String hql = "from WxRoom l where l.ownerId=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, ownerId);
		return (WxRoom) query.uniqueResult();
	}

}
