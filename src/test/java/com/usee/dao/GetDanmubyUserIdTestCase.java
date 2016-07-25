package com.usee.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.minidev.json.JSONArray;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.usee.dao.impl.DanmuDaoImp;

@RunWith(SpringJUnit4ClassRunner.class)    
@ContextHierarchy({
    @ContextConfiguration(name = "parent", locations = "classpath:spring-common.xml"),
    @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
})   
@Transactional
public class GetDanmubyUserIdTestCase {
	
	@Resource
	private SessionFactory sessionFactory;
	@Autowired
	private DanmuDaoImp danmudao;

	@Test
	public void testGetDanmubyUserTest(){
		String hql = "select topicID from Danmu where userId =?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, "3");
		JSONArray jsonArray = query.list();
		Assert.assertEquals(list[0], "4");
	}
}
